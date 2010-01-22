/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.model;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class AnnotationXmlItem extends AbstractXmlItem
{

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
   public AnnotationXmlItem(XmlItem parent, Class<?> c, String innerText, Map<String, String> attributes)
   {
      super(XmlItemType.ANNOTATION, parent, c, innerText, attributes);
      if (innerText != null)
      {
         if (!innerText.trim().equals(""))
         {
            attributes.put("value", innerText);
         }
      }
   }

   public Set<XmlItemType> getAllowedItem()
   {
      return Collections.emptySet();
   }

}
