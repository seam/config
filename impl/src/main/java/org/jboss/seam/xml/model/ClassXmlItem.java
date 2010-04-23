/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.jboss.seam.xml.util.XmlConfigurationException;
import org.jboss.weld.extensions.util.ReflectionUtils;

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
      allowed.add(XmlItemType.TYPE);
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

         Field field = ReflectionUtils.getField(getJavaClass(), e.getKey());
         if (field != null)
         {
            values.add(new FieldXmlItem(this, field, e.getValue(), document, lineno));
         }
         else
         {
            String methodName = "set" + Character.toUpperCase(e.getKey().charAt(0)) + e.getKey().substring(1);
            if (ReflectionUtils.methodExists(getJavaClass(), methodName))
            {
               Set<Method> methods = ReflectionUtils.getMethods(getJavaClass());
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
