/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.seam.xml.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

import org.jboss.seam.xml.core.BeanResult;
import org.jboss.seam.xml.core.BeanResultType;
import org.jboss.seam.xml.fieldset.FieldValueObject;
import org.jboss.seam.xml.util.TypeOccuranceInformation;
import org.jboss.seam.xml.util.XmlConfigurationException;
import org.jboss.weld.extensions.annotated.AnnotatedTypeBuilder;
import org.jboss.weld.extensions.util.Reflections;

public class ClassXmlItem extends AbstractXmlItem
{

   HashSet<TypeOccuranceInformation> allowed = new HashSet<TypeOccuranceInformation>();

   public ClassXmlItem(XmlItem parent, Class<?> c, Map<String, String> attributes, String document, int lineno)
   {
      super(XmlItemType.CLASS, parent, c, null, attributes, document, lineno);
      allowed.add(TypeOccuranceInformation.of(XmlItemType.ANNOTATION, null, null));
      allowed.add(TypeOccuranceInformation.of(XmlItemType.FIELD, null, null));
      allowed.add(TypeOccuranceInformation.of(XmlItemType.METHOD, null, null));
      allowed.add(TypeOccuranceInformation.of(XmlItemType.PARAMETERS, null, null));
      allowed.add(TypeOccuranceInformation.of(XmlItemType.REPLACE, null, null));
      allowed.add(TypeOccuranceInformation.of(XmlItemType.MODIFIES, null, null));
   }

   public Set<TypeOccuranceInformation> getAllowedItem()
   {
      return allowed;
   }

   public Set<FieldValueXmlItem> getShorthandFieldValues()
   {
      Set<FieldValueXmlItem> values = new HashSet<FieldValueXmlItem>();
      for (Entry<String, String> e : attributes.entrySet())
      {

         Field field = Reflections.getField(getJavaClass(), e.getKey());
         if (field != null)
         {
            values.add(new FieldXmlItem(this, field, e.getValue(), document, lineno));
         }
         else
         {
            String methodName = "set" + Character.toUpperCase(e.getKey().charAt(0)) + e.getKey().substring(1);
            if (Reflections.methodExists(getJavaClass(), methodName))
            {
               Set<Method> methods = Reflections.getAllMethods(getJavaClass());
               for (Method m : methods)
               {
                  if (m.getName().equals(methodName) && m.getParameterTypes().length == 1)
                  {
                     values.add(new PropertyXmlItem(this, e.getKey(), m, e.getValue(), document, lineno));
                     break;
                  }
               }
            }
            else
            {
               throw new XmlConfigurationException("Could not resolve field: " + e.getKey(), document, lineno);
            }
         }

      }
      return values;
   }

   public BeanResult<?> createBeanResult(BeanManager manager)
   {
      boolean override = !getChildrenOfType(ReplacesXmlItem.class).isEmpty();
      boolean extend = !getChildrenOfType(ModifiesXmlItem.class).isEmpty();
      BeanResultType beanType = BeanResultType.ADD;
      if (override && extend)
      {
         throw new XmlConfigurationException("A bean may not both <override> and <extend> an existing bean", getDocument(), getLineno());
      }
      if (override)
      {
         beanType = BeanResultType.REPLACES;
      }
      else if (extend)
      {
         beanType = BeanResultType.MODIFIES;
      }
      List<BeanResult> inlineBeans = new ArrayList<BeanResult>();
      // get all the field values from the bean
      Set<String> configuredFields = new HashSet<String>();
      List<FieldValueObject> fields = new ArrayList<FieldValueObject>();
      for (FieldValueXmlItem xi : getChildrenOfType(FieldValueXmlItem.class))
      {
         inlineBeans.addAll(xi.getInlineBeans());
         FieldValueObject f = xi.getFieldValue();
         if (f != null)
         {
            fields.add(f);
            configuredFields.add(xi.getFieldName());
         }
      }

      for (FieldValueXmlItem f : getShorthandFieldValues())
      {
         if (configuredFields.contains(f.getFieldName()))
         {
            throw new XmlConfigurationException("Field configured in two places: " + getJavaClass().getName() + "." + f.getFieldName(), getDocument(), getLineno());
         }
         fields.add(f.getFieldValue());
      }

      // if it is an extend we want to read the annotations from the underlying
      // class
      BeanResult<?> result = new BeanResult(getJavaClass(), extend, beanType, fields, inlineBeans);
      AnnotatedTypeBuilder<?> type = result.getBuilder();
      // list of constructor arguments
      List<ParameterXmlItem> constList = new ArrayList<ParameterXmlItem>();

      for (AnnotationXmlItem item : getChildrenOfType(AnnotationXmlItem.class))
      {
         Annotation a = AnnotationUtils.createAnnotation(item);
         type.addToClass(a);
      }
      List<ParametersXmlItem> constructorParameters = getChildrenOfType(ParametersXmlItem.class);
      if (constructorParameters.size() > 1)
      {
         throw new XmlConfigurationException("A method may only have a single <parameters> element", getDocument(), getLineno());
      }
      else if (!constructorParameters.isEmpty())
      {
         for (ParameterXmlItem item : constructorParameters.get(0).getChildrenOfType(ParameterXmlItem.class))
         {
            constList.add(item);
         }
      }
      for (FieldXmlItem item : getChildrenOfType(FieldXmlItem.class))
      {
         for (AnnotationXmlItem fi : item.getChildrenOfType(AnnotationXmlItem.class))
         {
            Annotation a = AnnotationUtils.createAnnotation(fi);
            type.addToField(item.getField(), a);
         }
      }
      for (MethodXmlItem item : getChildrenOfType(MethodXmlItem.class))
      {
         int paramCount = 0;

         for (AnnotationXmlItem fi : item.getChildrenOfType(AnnotationXmlItem.class))
         {
            Annotation a = AnnotationUtils.createAnnotation(fi);
            type.addToMethod(item.getMethod(), a);
         }
         List<ParametersXmlItem> parameters = item.getChildrenOfType(ParametersXmlItem.class);
         if (parameters.size() > 1)
         {
            throw new XmlConfigurationException("A method may only have a single <parameters> element", item.getDocument(), item.getLineno());
         }
         else if (!parameters.isEmpty())
         {
            for (ParameterXmlItem fi : parameters.get(0).getChildrenOfType(ParameterXmlItem.class))
            {
               int param = paramCount++;
               for (AnnotationXmlItem pan : fi.getChildrenOfType(AnnotationXmlItem.class))
               {
                  Annotation a = AnnotationUtils.createAnnotation(pan);
                  type.addToMethodParameter(item.getMethod(), param, a);
               }
            }
         }

      }

      if (!constList.isEmpty())
      {
         int paramCount = 0;
         Constructor<?> c = resolveConstructor(constList);
         // we automatically add inject to the constructor
         type.addToConstructor((Constructor) c, new AnnotationLiteral<Inject>()
         {
         });
         for (ParameterXmlItem fi : constList)
         {
            int param = paramCount++;
            for (AnnotationXmlItem pan : fi.getChildrenOfType(AnnotationXmlItem.class))
            {
               Annotation a = AnnotationUtils.createAnnotation(pan);
               type.addToConstructorParameter((Constructor) c, param, a);
            }
         }
      }
      return result;
   }

   private Constructor<?> resolveConstructor(List<ParameterXmlItem> constList)
   {
      Class<?>[] params = new Class[constList.size()];
      for (int i = 0; i < constList.size(); ++i)
      {
         params[i] = constList.get(i).getJavaClass();
      }
      Constructor<?> ret = Reflections.getConstructor(getJavaClass(), params);
      if (ret == null)
      {
         throw new XmlConfigurationException("Could not resolve constructor for " + getJavaClass() + " with arguments " + params, getDocument(), getLineno());
      }
      return ret;
   }

}
