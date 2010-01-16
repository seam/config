/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.model;

import java.util.Collections;
import java.util.Set;

public class VetoXmlItem extends AbstractXmlItem
{

   public VetoXmlItem(XmlItem parent)
   {
      super(XmlItemType.VETO, parent, null, null, null);

   }

   public Set<XmlItemType> getAllowedItem()
   {
      return Collections.singleton(XmlItemType.CLASS);
   }

}
