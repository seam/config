/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.seam.xml.fieldset.FieldValueObject;
import org.jboss.seam.xml.util.XmlConfigurationException;

public class ArrayXmlItem implements XmlItem
{

   XmlItem child = null, parent;

   Class<?> javaClass;

   final String document;

   final int lineno;

   public ArrayXmlItem(XmlItem parent, String document, int lineno)
   {
      allowed.add(XmlItemType.CLASS);
      this.parent = parent;
      this.document = document;
      this.lineno = lineno;
   }

   public String getDocument()
   {
      return document;
   }

   public int getLineno()
   {
      return lineno;
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
         throw new XmlConfigurationException("Array elements can only have one child", getDocument(), getLineno());
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
         throw new XmlConfigurationException("<array>  element must have a child specifying the array type", getDocument(), getLineno());
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
            throw new XmlConfigurationException("Cannot create array class from " + l.getName(), getDocument(), getLineno());
         }
      }
      return true;
   }
   public <T> List<T> getChildrenOfType(Class<T> type)
   {
      List<T> ret = new ArrayList<T>();
      for(XmlItem i : getChildren())
      {
         if(type.isAssignableFrom(i.getClass()))
         {
            ret.add((T)i);
         }
      }
      return ret;
   }
   
}
