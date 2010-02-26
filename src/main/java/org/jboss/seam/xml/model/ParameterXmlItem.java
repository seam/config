/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.model;

import java.util.HashSet;
import java.util.Set;

public class ParameterXmlItem extends AbstractXmlItem
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
   public ParameterXmlItem(XmlItem parent, Class<?> c, String document, int lineno)
   {
      super(XmlItemType.PARAMETER, parent, c, null, null, document, lineno);
      allowed.add(XmlItemType.ANNOTATION);

   }

   public Set<XmlItemType> getAllowedItem()
   {
      return allowed;
   }

}
