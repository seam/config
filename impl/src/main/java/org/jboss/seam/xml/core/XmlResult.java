/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.core;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.seam.xml.fieldset.FieldValueObject;

/**
 * Stores the result of parsing an XML document
 * 
 * @author Stuart Douglas <stuart@baileyroberts.com.au>
 * 
 */
public class XmlResult
{

   private Map<Class<? extends Annotation>, Annotation[]> stereotypes = new HashMap<Class<? extends Annotation>, Annotation[]>();

   private List<Class<? extends Annotation>> qualifiers = new ArrayList<Class<? extends Annotation>>();

   private List<Class<? extends Annotation>> interceptorBindings = new ArrayList<Class<? extends Annotation>>();

   private List<Class<?>> veto = new ArrayList<Class<?>>();

   private List<String> problems = new ArrayList<String>();

   private List<BeanResult<?>> beans = new ArrayList<BeanResult<?>>();

   private List<BeanResult<?>> interfaces = new ArrayList<BeanResult<?>>();

   private List<GenericBeanResult> genericBeans = new ArrayList<GenericBeanResult>();

   private Map<BeanResult<?>, List<FieldValueObject>> fieldValues = new HashMap<BeanResult<?>, List<FieldValueObject>>();

   private Map<Class<?>, List<FieldValueObject>> interfaceFieldValues = new HashMap<Class<?>, List<FieldValueObject>>();

   public void addStereotype(Class<? extends Annotation> an, Annotation[] values)
   {
      stereotypes.put(an, values);
   }

   public Map<Class<? extends Annotation>, Annotation[]> getStereotypes()
   {
      return stereotypes;
   }

   public void addQualifier(Class<? extends Annotation> qualifier)
   {
      qualifiers.add(qualifier);
   }

   public List<Class<? extends Annotation>> getQualifiers()
   {
      return qualifiers;
   }

   public void addInterceptorBinding(Class<? extends Annotation> binding)
   {
      interceptorBindings.add(binding);
   }

   public List<Class<? extends Annotation>> getInterceptorBindings()
   {
      return interceptorBindings;
   }

   public void addBean(BeanResult<?> bean)
   {
      beans.add(bean);
   }

   public List<BeanResult<?>> getBeans()
   {
      return beans;
   }

   public List<String> getProblems()
   {
      return problems;
   }

   public void addProblem(String p)
   {
      problems.add(p);
   }

   public void addFieldValue(BeanResult<?> res, List<FieldValueObject> list)
   {
      fieldValues.put(res, list);
   }

   public Map<BeanResult<?>, List<FieldValueObject>> getFieldValues()
   {
      return fieldValues;
   }

   public void addVeto(Class<?> clazz)
   {
      veto.add(clazz);
   }

   public List<Class<?>> getVeto()
   {
      return veto;
   }

   public void addInterface(BeanResult<?> bean)
   {
      interfaces.add(bean);
   }

   public List<BeanResult<?>> getInterfaces()
   {
      return interfaces;
   }

   public void addInterfaceFieldValues(Class<?> clazz, List<FieldValueObject> values)
   {
      interfaceFieldValues.put(clazz, values);
   }

   public Map<Class<?>, List<FieldValueObject>> getInterfaceFieldValues()
   {
      return interfaceFieldValues;
   }

   public void addGenericBean(GenericBeanResult result)
   {
      genericBeans.add(result);
   }

   public List<GenericBeanResult> getGenericBeans()
   {
      return genericBeans;
   }

}
