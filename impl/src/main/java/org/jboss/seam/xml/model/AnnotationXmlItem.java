/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.model;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AnnotationXmlItem extends AbstractXmlItem
{
   HashSet<XmlItemType> allowed = new HashSet<XmlItemType>();

   public AnnotationXmlItem(XmlItem parent, Class<?> c, String innerText, Map<String, String> attributes, String document, int lineno)
   {
      super(XmlItemType.ANNOTATION, parent, c, innerText, attributes, document, lineno);
      if (innerText != null)
      {
         if (!innerText.trim().equals(""))
         {
            attributes.put("value", innerText);
         }
      }
      allowed.add(XmlItemType.ANNOTATION);
   }

   public Set<XmlItemType> getAllowedItem()
   {
      return allowed;
   }

}
