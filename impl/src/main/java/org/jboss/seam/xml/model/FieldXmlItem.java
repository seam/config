/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.seam.xml.fieldset.ArrayFieldSet;
import org.jboss.seam.xml.fieldset.CollectionFieldSet;
import org.jboss.seam.xml.fieldset.DirectFieldSetter;
import org.jboss.seam.xml.fieldset.FieldValueObject;
import org.jboss.seam.xml.fieldset.FieldValueSetter;
import org.jboss.seam.xml.fieldset.MapFieldSet;
import org.jboss.seam.xml.fieldset.MethodFieldSetter;
import org.jboss.seam.xml.fieldset.SimpleFieldValue;
import org.jboss.seam.xml.util.XmlConfigurationException;

public class FieldXmlItem extends AbstractXmlItem
{

   FieldValueSetter fieldSetter;
   FieldValueObject fieldValue;
   Field field;
   HashSet<XmlItemType> allowed = new HashSet<XmlItemType>();

   public FieldXmlItem(XmlItem parent, Field c, String innerText, String document, int lineno)
   {
      super(XmlItemType.FIELD, parent, parent.getJavaClass(), innerText, null, document, lineno);
      this.field = c;
      this.fieldSetter = getFieldValueSetter(c);
      if (innerText != null && innerText.length() > 0)
      {
         fieldValue = new SimpleFieldValue(parent.getJavaClass(), fieldSetter, innerText);
      }
      allowed.add(XmlItemType.ANNOTATION);
      allowed.add(XmlItemType.VALUE);
      allowed.add(XmlItemType.TYPE);
   }

   public Field getField()
   {
      return field;
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
         if (Map.class.isAssignableFrom(field.getType()))
         {
            if (!valueEntries.isEmpty())
            {
               throw new XmlConfigurationException("Map fields cannot have <value> elements as children,only <entry> elements Field:" + field.getDeclaringClass().getName() + '.' + field.getName(), getDocument(), getLineno());
            }
            if (!mapEntries.isEmpty())
            {
               fieldValue = new MapFieldSet(fieldSetter, mapEntries);
            }
         }
         else if (Collection.class.isAssignableFrom(field.getType()) || field.getType().isArray())
         {
            if (!mapEntries.isEmpty())
            {
               throw new XmlConfigurationException("Collection fields must be set using <value> not <entry> Field:" + field.getDeclaringClass().getName() + '.' + field.getName(), getDocument(), getLineno());
            }
            if (!valueEntries.isEmpty())
            {
               if (field.getType().isArray())
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
               throw new XmlConfigurationException("Only Map fields can be set using <entry> Field:" + field.getDeclaringClass().getName() + '.' + field.getName(), getDocument(), getLineno());
            }
            if (valueEntries.size() != 1)
            {
               throw new XmlConfigurationException("Non collection fields can only have a single <value> element Field:" + field.getDeclaringClass().getName() + '.' + field.getName(), getDocument(), getLineno());
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

   FieldValueSetter getFieldValueSetter(Field field)
   {
      String fieldName = field.getName();
      String methodName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
      Method setter = null;
      try
      {
         setter = field.getDeclaringClass().getMethod(methodName, field.getType());
      }
      catch (SecurityException e)
      {

      }
      catch (NoSuchMethodException e)
      {

      }
      if (setter != null)
      {
         return new MethodFieldSetter(setter);
      }
      return new DirectFieldSetter(field);
   }
}
