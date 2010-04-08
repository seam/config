/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.model;

import java.util.Collections;
import java.util.Set;

public class SpecializesXmlItem extends AbstractXmlItem
{

   public SpecializesXmlItem(XmlItem parent, String document, int lineno)
   {
      super(XmlItemType.SPECIALIZES, parent, null, null, null, document, lineno);

   }

   public Set<XmlItemType> getAllowedItem()
   {
      return Collections.emptySet();
   }

}
