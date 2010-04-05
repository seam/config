/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.seam.xml.fieldset.ArrayFieldSet;
import org.jboss.seam.xml.fieldset.CollectionFieldSet;
import org.jboss.seam.xml.fieldset.FieldValueObject;
import org.jboss.seam.xml.fieldset.FieldValueSetter;
import org.jboss.seam.xml.fieldset.MapFieldSet;
import org.jboss.seam.xml.fieldset.MethodFieldSetter;
import org.jboss.seam.xml.fieldset.SimpleFieldValue;
import org.jboss.seam.xml.util.XmlConfigurationException;

public class PropertyXmlItem extends AbstractXmlItem
{

   FieldValueSetter fieldSetter;
   FieldValueObject fieldValue;
   String name;
   Class<?> type;
   HashSet<XmlItemType> allowed = new HashSet<XmlItemType>();

   public PropertyXmlItem(XmlItem parent, String name, Method setter, String innerText, String document, int lineno)
   {
      super(XmlItemType.FIELD, parent, parent.getJavaClass(), innerText, null, document, lineno);
      this.name = name;
      this.type = setter.getParameterTypes()[0];
      this.fieldSetter = new MethodFieldSetter(setter);
      if (innerText != null && innerText.length() > 0)
      {
         fieldValue = new SimpleFieldValue(parent.getJavaClass(), fieldSetter, innerText);
      }
      allowed.add(XmlItemType.VALUE);
   }

   public FieldValueObject getFieldValue()
   {
      return fieldValue;
   }

   @Override
   public boolean resolveChildren()
   {
      List<EntryXmlItem> mapEntries = new ArrayList<EntryXmlItem>();
      List<XmlItem> valueEntries = new ArrayList<XmlItem>();
      if (fieldValue == null)
      {
         for (XmlItem i : children)
         {
            if (i.getType() == XmlItemType.VALUE)
            {
               valueEntries.add(i);
            }
            else if (i.getType() == XmlItemType.ENTRY)
            {
               mapEntries.add((EntryXmlItem) i);
            }

         }
      }
      if (!mapEntries.isEmpty() || !valueEntries.isEmpty())
      {
         if (Map.class.isAssignableFrom(type))
         {
            if (!valueEntries.isEmpty())
            {
               throw new XmlConfigurationException("Map fields cannot have <value> elements as children,only <entry> elements Field:" + parent.getJavaClass().getName() + '.' + name, getDocument(), getLineno());
            }
            if (!mapEntries.isEmpty())
            {
               fieldValue = new MapFieldSet(fieldSetter, mapEntries);
            }
         }
         else if (Collection.class.isAssignableFrom(type) || type.isArray())
         {
            if (!mapEntries.isEmpty())
            {
               throw new XmlConfigurationException("Collection fields must be set using <value> not <entry> Field:" + parent.getJavaClass().getName() + '.' + name, getDocument(), getLineno());
            }
            if (!valueEntries.isEmpty())
            {
               if (type.isArray())
               {
                  fieldValue = new ArrayFieldSet(fieldSetter, valueEntries);
               }
               else
               {
                  fieldValue = new CollectionFieldSet(fieldSetter, valueEntries);
               }
            }
         }
         else
         {
            if (!mapEntries.isEmpty())
            {
               throw new XmlConfigurationException("Only Map fields can be set using <entry> Field:" + parent.getJavaClass().getName() + '.' + name, getDocument(), getLineno());
            }
            if (valueEntries.size() != 1)
            {
               throw new XmlConfigurationException("Non collection fields can only have a single <value> element Field:" + parent.getJavaClass().getName() + '.' + name, getDocument(), getLineno());
            }
            fieldValue = new SimpleFieldValue(parent.getJavaClass(), fieldSetter, valueEntries.get(0).getInnerText());
         }
      }
      return true;
   }

   public Set<XmlItemType> getAllowedItem()
   {
      return allowed;
   }

}
