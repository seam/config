/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.model;

import java.util.HashSet;
import java.util.Set;

import org.jboss.seam.xml.util.XmlConfigurationException;

public class EntryXmlItem extends AbstractXmlItem
{

   final Set<XmlItemType> allowed = new HashSet<XmlItemType>();

   XmlItem key;
   XmlItem value;

   public EntryXmlItem(XmlItem parent, String document, int lineno)
   {
      super(XmlItemType.ENTRY, parent, null, null, null, document, lineno);
      allowed.add(XmlItemType.VALUE);
      allowed.add(XmlItemType.KEY);
   }

   public Set<XmlItemType> getAllowedItem()
   {
      return allowed;
   }

   @Override
   public boolean resolveChildren()
   {
      if (children.size() != 2)
      {
         throw new XmlConfigurationException("<entry> tags must have two children, a <key> and a <value>", getDocument(), getLineno());
      }
      for (XmlItem i : children)
      {
         if (i.getType() == XmlItemType.VALUE)
         {
            if (value != null)
            {
               throw new XmlConfigurationException("<entry> tags must have two children, a <key> and a <value>", getDocument(), getLineno());
            }
            value = i;
         }
         else if (i.getType() == XmlItemType.KEY)
         {
            if (key != null)
            {
               throw new XmlConfigurationException("<entry> tags must have two children, a <key> and a <value>", getDocument(), getLineno());
            }
            key = i;
         }
      }
      return true;
   }

   public XmlItem getKey()
   {
      return key;
   }

   public XmlItem getValue()
   {
      return value;
   }

}
