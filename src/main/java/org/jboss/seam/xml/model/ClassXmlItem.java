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

   public ClassXmlItem(XmlItem parent, Class<?> c, String document, int lineno)
   {
      super(XmlItemType.CLASS, parent, c, null, null, document, lineno);
      allowed.add(XmlItemType.ANNOTATION);
      allowed.add(XmlItemType.FIELD);
      allowed.add(XmlItemType.METHOD);
      allowed.add(XmlItemType.PARAMETERS);

   }

   public Set<XmlItemType> getAllowedItem()
   {
      return allowed;
   }

}
