/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.model;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

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

   public Set<XmlItemType> getAllowedItem()
   {
      return Collections.singleton(XmlItemType.CLASS);
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
