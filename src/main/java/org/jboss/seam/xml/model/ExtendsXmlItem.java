/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.model;

import java.util.Collections;
import java.util.Set;

public class ExtendsXmlItem extends AbstractXmlItem
{

   public ExtendsXmlItem(XmlItem parent, String document, int lineno)
   {
      super(XmlItemType.EXTENDS, parent, null, null, null, document, lineno);

   }

   public Set<XmlItemType> getAllowedItem()
   {
      return Collections.emptySet();
   }

}
