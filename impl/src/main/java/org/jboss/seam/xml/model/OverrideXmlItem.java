/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.model;

import java.util.Collections;
import java.util.Set;

public class OverrideXmlItem extends AbstractXmlItem
{

   public OverrideXmlItem(XmlItem parent, String document, int lineno)
   {
      super(XmlItemType.OVERRIDE, parent, null, null, null, document, lineno);

   }

   public Set<XmlItemType> getAllowedItem()
   {
      return Collections.emptySet();
   }

}
