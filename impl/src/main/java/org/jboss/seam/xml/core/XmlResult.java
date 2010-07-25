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
package org.jboss.seam.xml.core;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores the result of parsing an XML document
 * 
 * @author Stuart Douglas <stuart@baileyroberts.com.au>
 * 
 */
public class XmlResult implements Comparable<XmlResult>
{

   private final Map<Class<? extends Annotation>, Annotation[]> stereotypes = new HashMap<Class<? extends Annotation>, Annotation[]>();

   private final List<Class<? extends Annotation>> qualifiers = new ArrayList<Class<? extends Annotation>>();

   private final List<Class<? extends Annotation>> interceptorBindings = new ArrayList<Class<? extends Annotation>>();

   private final List<Class<?>> veto = new ArrayList<Class<?>>();

   private final List<String> problems = new ArrayList<String>();

   private final List<BeanResult<?>> beans = new ArrayList<BeanResult<?>>();

   private final List<GenericBeanResult> genericBeans = new ArrayList<GenericBeanResult>();

   private final String sortKey;

   public XmlResult(String fileUrl)
   {
      StringBuilder keyBuilder = new StringBuilder(fileUrl.length());
      for (int i = fileUrl.length() - 1; i >= 0; --i)
      {
         keyBuilder.append(fileUrl.charAt(i));
      }
      sortKey = keyBuilder.toString();
   }

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

   /**
    * Gets all beans from the result, including inline beans
    * 
    * @return
    */
   public List<BeanResult<?>> getFlattenedBeans()
   {
      List<BeanResult<?>> results = new ArrayList<BeanResult<?>>();
      for (BeanResult<?> a : beans)
      {
         getFlattenedBeans(a, results);
      }
      return results;
   }

   private void getFlattenedBeans(BeanResult<?> r, List<BeanResult<?>> results)
   {
      results.add(r);
      for (BeanResult<?> a : r.getInlineBeans())
      {
         getFlattenedBeans(a, results);
      }
   }

   public List<String> getProblems()
   {
      return problems;
   }

   public void addProblem(String p)
   {
      problems.add(p);
   }

   public void addVeto(Class<?> clazz)
   {
      veto.add(clazz);
   }

   public List<Class<?>> getVeto()
   {
      return veto;
   }

   public void addGenericBean(GenericBeanResult result)
   {
      genericBeans.add(result);
   }

   public List<GenericBeanResult> getGenericBeans()
   {
      return genericBeans;
   }

   public int compareTo(XmlResult o)
   {
      return sortKey.compareTo(o.sortKey);
   }

}
