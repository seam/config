/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.model;

import java.util.Collections;
import java.util.Set;

public class DependsXmlItem extends AbstractXmlItem
{

   public DependsXmlItem(XmlItem parent, String innerTest, String document, int lineno)
   {
      super(XmlItemType.DEPENDENCY, parent, null, innerTest, null, document, lineno);
   }

   public Set<XmlItemType> getAllowedItem()
   {
      return Collections.emptySet();
   }

}
