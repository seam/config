/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.model;

import java.util.HashSet;
import java.util.Set;

public class ClassXmlItem extends AbstractXmlItem
{

   HashSet<XmlItemType> allowed = new HashSet<XmlItemType>();

   /**
    * create a class note. If the class is an annotation in can have inner text
    * to represent the 'value' of the annotation, if can also have atrributes to
    * represent other properties
    * 
    * @param parent
    * @param c
    * @param innerText
    * @param attributes
    */
   public ClassXmlItem(XmlItem parent, Class<?> c, String document, int lineno)
   {
      super(XmlItemType.CLASS, parent, c, null, null, document, lineno);
      if (innerText != null)
      {
         if (!innerText.trim().equals(""))
         {
            attributes.put("value", innerText);
         }
      }
      allowed.add(XmlItemType.ANNOTATION);
      allowed.add(XmlItemType.FIELD);
      allowed.add(XmlItemType.METHOD);
      allowed.add(XmlItemType.CLASS);

   }

   public Set<XmlItemType> getAllowedItem()
   {
      return allowed;
   }

}
