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

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

import org.jboss.seam.xml.util.XmlConfigurationException;

public class ArrayXmlItem extends ParameterXmlItem
{

   Class javaClass = null;

   int dimensions = 1;

   public ArrayXmlItem(XmlItem parent, Map<String, String> attributes, String document, int lineno)
   {
      super(parent, null, document, lineno);
      if (attributes.containsKey("dimensions"))
      {
         try
         {
            dimensions = Integer.parseInt(attributes.get("dimensions"));
         }
         catch (NumberFormatException e)
         {
            throw new XmlConfigurationException("dimensions attribute on <array> must be an integer", document, lineno);
         }
      }
   }

   public boolean resolveChildren()
   {
      List<ClassXmlItem> classXmlItems = getChildrenOfType(ClassXmlItem.class);
      if (classXmlItems.isEmpty())
      {
         throw new XmlConfigurationException("<array>  element must have a child specifying the array type", getDocument(), getLineno());
      }
      else if (classXmlItems.size() != 1)
      {
         throw new XmlConfigurationException("<array>  element must have a single child specifying the array type", getDocument(), getLineno());
      }
      int[] dims = new int[dimensions];
      for (int i = 0; i < dimensions; ++i)
      {
         dims[i] = 0;
      }
      Class<?> l = classXmlItems.get(0).getJavaClass();
      javaClass = Array.newInstance(l, dims).getClass();

      return true;
   }

   @Override
   public Class<?> getJavaClass()
   {
      return javaClass;
   }

}
