/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.model;

import java.util.Collections;
import java.util.Set;

public class ParametersXmlItem extends AbstractXmlItem
{

   public ParametersXmlItem(XmlItem parent, String document, int lineno)
   {
      super(XmlItemType.PARAMETERS, parent, null, null, null, document, lineno);

   }

   public Set<XmlItemType> getAllowedItem()
   {
      return Collections.singleton(XmlItemType.PARAMETER);
   }

}
