/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.model;

import java.util.Collections;
import java.util.Set;

public class VetoXmlItem extends AbstractXmlItem
{

   public VetoXmlItem(XmlItem parent, String document, int lineno)
   {
      super(XmlItemType.VETO, parent, null, null, null, document, lineno);

   }

   public Set<XmlItemType> getAllowedItem()
   {
      return Collections.singleton(XmlItemType.CLASS);
   }

}
