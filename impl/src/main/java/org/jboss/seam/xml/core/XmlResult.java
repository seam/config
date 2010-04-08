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

   Map<Class<? extends Annotation>, Annotation[]> stereotypes = new HashMap<Class<? extends Annotation>, Annotation[]>();

   List<Class<? extends Annotation>> qualifiers = new ArrayList<Class<? extends Annotation>>();

   List<Class<? extends Annotation>> interceptorBindings = new ArrayList<Class<? extends Annotation>>();

   List<Class<?>> veto = new ArrayList<Class<?>>();

   List<String> problems = new ArrayList<String>();

   List<BeanResult<?>> beans = new ArrayList<BeanResult<?>>();

   List<BeanResult<?>> interfaces = new ArrayList<BeanResult<?>>();

   Map<BeanResult<?>, List<FieldValueObject>> fieldValues = new HashMap<BeanResult<?>, List<FieldValueObject>>();

   Map<Class<?>, List<FieldValueObject>> interfaceFieldValues = new HashMap<Class<?>, List<FieldValueObject>>();

   public Map<Class<? extends Annotation>, Annotation[]> getStereotypes()
   {
      return stereotypes;
   }

   public List<Class<? extends Annotation>> getQualifiers()
   {
      return qualifiers;
   }

   public List<Class<? extends Annotation>> getInterceptorBindings()
   {
      return interceptorBindings;
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

   public List<BeanResult<?>> getInterfaces()
   {
      return interfaces;
   }

   public Map<Class<?>, List<FieldValueObject>> getInterfaceFieldValues()
   {
      return interfaceFieldValues;
   }

}
