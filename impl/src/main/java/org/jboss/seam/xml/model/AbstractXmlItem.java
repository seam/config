/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractXmlItem implements XmlItem
{
   final XmlItemType type;
   final XmlItem parent;
   final Class<?> javaClass;

   final String innerText;
   final Map<String, String> attributes;

   final int lineno;

   final String document;

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
         this.attributes = attributes;
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

   public boolean resolveChildren()
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
