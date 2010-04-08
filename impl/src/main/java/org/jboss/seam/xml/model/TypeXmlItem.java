/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.model;

import java.util.HashSet;
import java.util.Set;

public class TypeXmlItem extends AbstractXmlItem
{

   static final Set<XmlItemType> allowed = new HashSet<XmlItemType>();

   static
   {
      allowed.add(XmlItemType.CLASS);
      allowed.add(XmlItemType.ANNOTATION);
   }

   public TypeXmlItem(XmlItem parent, String document, int lineno)
   {
      super(XmlItemType.TYPE, parent, null, null, null, document, lineno);
   }

   public Set<XmlItemType> getAllowedItem()
   {
      return allowed;
   }
}
