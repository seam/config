/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.model;

import java.util.Collections;
import java.util.Set;

public class KeyXmlItem extends AbstractXmlItem
{

   public KeyXmlItem(XmlItem parent, String innerText)
   {
      super(XmlItemType.KEY, parent, null, innerText, null);
   }

   public Set<XmlItemType> getAllowedItem()
   {
      return Collections.emptySet();
   }
}
