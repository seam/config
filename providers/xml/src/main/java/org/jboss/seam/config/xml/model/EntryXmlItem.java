/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.seam.config.xml.model;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.inject.spi.BeanManager;

import org.jboss.seam.config.xml.core.BeanResult;
import org.jboss.seam.config.xml.util.TypeOccuranceInformation;
import org.jboss.seam.config.xml.util.XmlConfigurationException;

public class EntryXmlItem extends AbstractXmlItem
{

   final Set<TypeOccuranceInformation> allowed = new HashSet<TypeOccuranceInformation>();

   KeyXmlItem key;
   ValueXmlItem value;

   public EntryXmlItem(XmlItem parent, String document, int lineno)
   {
      super(XmlItemType.ENTRY, parent, null, null, null, document, lineno);
      allowed.add(TypeOccuranceInformation.of(XmlItemType.VALUE, 1, 1));
      allowed.add(TypeOccuranceInformation.of(XmlItemType.KEY, 1, 1));
   }

   public Set<TypeOccuranceInformation> getAllowedItem()
   {
      return allowed;
   }

   @Override
   public boolean resolveChildren(BeanManager manager)
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
            value = (ValueXmlItem) i;
         }
         else if (i.getType() == XmlItemType.KEY)
         {
            if (key != null)
            {
               throw new XmlConfigurationException("<entry> tags must have two children, a <key> and a <value>", getDocument(), getLineno());
            }
            key = (KeyXmlItem) i;
         }
      }
      return true;
   }

   public KeyXmlItem getKey()
   {
      return key;
   }

   public ValueXmlItem getValue()
   {
      return (ValueXmlItem) value;
   }

   /**
    * get the inline beans for the value and the key
    * 
    * @param manager
    * @return
    */
   public Set<BeanResult<?>> getBeanResults(BeanManager manager)
   {
      Set<BeanResult<?>> ret = new HashSet<BeanResult<?>>();
      BeanResult<?> r = value.getBeanResult(manager);
      if (r != null)
      {
         ret.add(r);
      }
      r = key.getBeanResult(manager);
      if (r != null)
      {
         ret.add(r);
      }
      return ret;
   }

}
