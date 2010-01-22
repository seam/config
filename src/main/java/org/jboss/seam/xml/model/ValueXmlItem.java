/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.model;

import java.util.Collections;
import java.util.Set;

public class ValueXmlItem extends AbstractXmlItem
{

   public ValueXmlItem(XmlItem parent, String innerText)
   {
      super(XmlItemType.VALUE, parent, null, innerText, null);
   }

   public Set<XmlItemType> getAllowedItem()
   {
      return Collections.emptySet();
   }
}
