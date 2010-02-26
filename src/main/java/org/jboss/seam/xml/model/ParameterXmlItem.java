/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.model;

import java.util.HashSet;
import java.util.Set;

/**
 * represents a parameter of a constructor or method
 * 
 * @author Stuart Douglas <stuart@baileyroberts.com.au>
 * 
 */
public class ParameterXmlItem extends AbstractXmlItem
{
   HashSet<XmlItemType> allowed = new HashSet<XmlItemType>();

   public ParameterXmlItem(XmlItem parent, Class<?> c, String document, int lineno)
   {
      super(XmlItemType.PARAMETER, parent, c, null, null, document, lineno);
      allowed.add(XmlItemType.ANNOTATION);
   }

   public Set<XmlItemType> getAllowedItem()
   {
      return allowed;
   }

}
