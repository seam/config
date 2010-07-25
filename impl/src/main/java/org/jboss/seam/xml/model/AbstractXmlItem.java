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
package org.jboss.seam.xml.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.spi.BeanManager;

public abstract class AbstractXmlItem implements XmlItem
{
   protected final XmlItemType type;
   protected final XmlItem parent;
   protected final Class<?> javaClass;

   protected final String innerText;
   protected final Map<String, String> attributes;

   protected final int lineno;

   protected final String document;

   public String getInnerText()
   {
      return innerText;
   }

   public AbstractXmlItem(XmlItemType type, XmlItem parent, Class<?> javaClass, String innerText, Map<String, String> attributes, String document, int lineno)
   {
      this.type = type;
      this.parent = parent;
      this.javaClass = javaClass;
      this.innerText = innerText;
      if (attributes == null)
      {
         this.attributes = new HashMap<String, String>();
      }
      else
      {
         this.attributes = new HashMap<String, String>(attributes);
      }
      this.lineno = lineno;
      this.document = document;
   }

   public int getLineno()
   {
      return lineno;
   }

   public String getDocument()
   {
      return document;
   }

   final List<XmlItem> children = new ArrayList<XmlItem>();

   public void addChild(XmlItem xmlItem)
   {
      children.add(xmlItem);
   }

   public XmlItem getParent()
   {
      return parent;
   }

   public List<XmlItem> getChildren()
   {
      return Collections.unmodifiableList(children);
   }

   public XmlItemType getType()
   {
      return type;
   }

   public Class<?> getJavaClass()
   {
      return javaClass;
   }

   public boolean resolveChildren(BeanManager manager)
   {
      return true;
   }

   public Map<String, String> getAttributes()
   {
      return attributes;
   }

   public <T> List<T> getChildrenOfType(Class<T> type)
   {
      List<T> ret = new ArrayList<T>();
      for (XmlItem i : children)
      {
         if (type.isAssignableFrom(i.getClass()))
         {
            ret.add((T) i);
         }
      }
      return ret;
   }
}
