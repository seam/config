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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.jboss.seam.xml.util.XmlConfigurationException;
import org.jboss.weld.extensions.util.Reflections;

public class ClassXmlItem extends AbstractXmlItem
{

   HashSet<XmlItemType> allowed = new HashSet<XmlItemType>();

   public ClassXmlItem(XmlItem parent, Class<?> c, Map<String, String> attributes, String document, int lineno)
   {
      super(XmlItemType.CLASS, parent, c, null, attributes, document, lineno);
      allowed.add(XmlItemType.ANNOTATION);
      allowed.add(XmlItemType.FIELD);
      allowed.add(XmlItemType.METHOD);
      allowed.add(XmlItemType.PARAMETERS);
   }

   public Set<XmlItemType> getAllowedItem()
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
               Set<Method> methods = Reflections.getMethods(getJavaClass());
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

}
