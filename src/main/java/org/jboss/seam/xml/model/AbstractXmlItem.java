/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.seam.xml.fieldset.FieldValueObject;

public abstract class AbstractXmlItem implements XmlItem
{
   final XmlItemType type;
   final XmlItem parent;
   final Class<?> javaClass;

   final String innerText;
   final Map<String, String> attributes;

   public String getInnerText()
   {
      return innerText;
   }

   public AbstractXmlItem(XmlItemType type, XmlItem parent, Class<?> javaClass, String innerText, Map<String, String> attributes)
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

   public Field getField()
   {
      return null;
   }

   public Method getMethod()
   {
      return null;
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

   public FieldValueObject getFieldValue()
   {
      return null;
   }
}
