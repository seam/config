/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.seam.xml.fieldset.FieldValueObject;

public class ArrayXmlItem implements XmlItem
{

   XmlItem child = null, parent;

   Class<?> javaClass;

   public ArrayXmlItem(XmlItem parent)
   {
      allowed.add(XmlItemType.CLASS);
      this.parent = parent;
   }

   Set<XmlItemType> allowed = new HashSet<XmlItemType>();

   public Set<XmlItemType> getAllowedItem()
   {
      return allowed;
   }

   public void addChild(XmlItem xmlItem)
   {
      if (child != null)
      {
         throw new RuntimeException("Array elements can only have one child");
      }
      child = xmlItem;
   }

   @SuppressWarnings("unchecked")
   public Map<String, String> getAttributes()
   {
      return Collections.EMPTY_MAP;
   }

   public List<XmlItem> getChildren()
   {
      return Collections.singletonList(child);
   }

   public Field getField()
   {
      return null;
   }

   public FieldValueObject getFieldValue()
   {
      return null;
   }

   public String getInnerText()
   {
      return null;
   }

   public Class getJavaClass()
   {
      return javaClass;
   }

   public Method getMethod()
   {
      return null;
   }

   public XmlItem getParent()
   {
      return parent;
   }

   public XmlItemType getType()
   {
      return XmlItemType.CLASS;
   }

   public boolean resolveChildren()
   {
      if (child == null)
      {
         throw new RuntimeException("<array>  element must have a child specifying the array type");
      }
      Class<?> l = child.getJavaClass();
      try
      {
         javaClass = getClass().getClassLoader().loadClass("[L" + l.getName() + ";");
      }
      catch (ClassNotFoundException e)
      {
         try
         {
            javaClass = Thread.currentThread().getContextClassLoader().loadClass("[L" + l.getName() + ";");
         }
         catch (ClassNotFoundException e2)
         {
            throw new RuntimeException("Cannot create array class from " + l.getName());
         }
      }
      return true;
   }

}
