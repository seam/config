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

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.jboss.seam.xml.util.TypeOccuranceInformation;
import org.jboss.seam.xml.util.XmlConfigurationException;

public class GenericBeanXmlItem extends AbstractXmlItem
{

   Class<?> javaClass;

   public GenericBeanXmlItem(XmlItem parent, Map<String, String> attributes, String document, int lineno)
   {
      super(XmlItemType.GENERIC_BEAN, parent, null, null, attributes, document, lineno);
      if (attributes.containsKey("class"))
      {
         javaClass = getClass(attributes.get("class"), document, lineno);
      }
      else
      {
         throw new XmlConfigurationException("<genericBean> element must have a class attribute", document, lineno);
      }
   }

   public Set<TypeOccuranceInformation> getAllowedItem()
   {
      return Collections.singleton(TypeOccuranceInformation.of(XmlItemType.CLASS, 1, null));
   }

   public Class<?> getClass(String className, String document, int lineno)
   {
      try
      {
         if (Thread.currentThread().getContextClassLoader() != null)
         {
            return Thread.currentThread().getContextClassLoader().loadClass(className);
         }
      }
      catch (ClassNotFoundException e)
      {

      }
      try
      {
         return getClass().getClassLoader().loadClass(className);
      }
      catch (ClassNotFoundException e1)
      {
         throw new XmlConfigurationException("could not find class <genericBean>", document, lineno);
      }
   }

   @Override
   public Class<?> getJavaClass()
   {
      return javaClass;
   }

}
