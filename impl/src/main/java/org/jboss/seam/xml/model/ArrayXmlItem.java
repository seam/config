/*
 * Distributed under the LGPL License
 * 
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
