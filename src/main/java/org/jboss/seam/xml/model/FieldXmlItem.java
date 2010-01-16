/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.seam.xml.fieldset.ArrayFieldSet;
import org.jboss.seam.xml.fieldset.CollectionFieldSet;
import org.jboss.seam.xml.fieldset.FieldValueObject;
import org.jboss.seam.xml.fieldset.MapFieldSet;
import org.jboss.seam.xml.fieldset.SimpleFieldValue;

public class FieldXmlItem extends AbstractXmlItem
{

   Field field;
   FieldValueObject fieldValue;
   HashSet<XmlItemType> allowed = new HashSet<XmlItemType>();

   public FieldXmlItem(XmlItem parent, Field c, String innerText)
   {
      super(XmlItemType.FIELD, parent, parent.getJavaClass(), innerText, null);
      this.field = c;
      if (innerText != null && innerText.length() > 0)
      {
         fieldValue = new SimpleFieldValue(parent.getJavaClass(), c, innerText);
      }
      allowed.add(XmlItemType.ANNOTATION);
      allowed.add(XmlItemType.VALUE);
   }

   public Field getField()
   {
      return field;
   }

   @Override
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
               throw new RuntimeException("Map fields cannot have <value> elements as children,only <entry> elements Field:" + field.getDeclaringClass().getName() + '.' + field.getName());
            }
            if (!mapEntries.isEmpty())
            {
               fieldValue = new MapFieldSet(field, mapEntries);
            }
         }
         else if (Collection.class.isAssignableFrom(field.getType()) || field.getType().isArray())
         {
            if (!mapEntries.isEmpty())
            {
               throw new RuntimeException("Collection fields must be set using <value> not <entry> Field:" + field.getDeclaringClass().getName() + '.' + field.getName());
            }
            if (!valueEntries.isEmpty())
            {
               if (field.getType().isArray())
               {
                  fieldValue = new ArrayFieldSet(field, valueEntries);
               }
               else
               {
                  fieldValue = new CollectionFieldSet(field, valueEntries);
               }
            }
         }
         else
         {
            if (!mapEntries.isEmpty())
            {
               throw new RuntimeException("Only Map fields can be set using <entry> Field:" + field.getDeclaringClass().getName() + '.' + field.getName());
            }
            if (valueEntries.size() != 1)
            {
               throw new RuntimeException("Non collection fields can only have a single <value> element Field:" + field.getDeclaringClass().getName() + '.' + field.getName());
            }
            fieldValue = new SimpleFieldValue(parent.getJavaClass(), field, valueEntries.get(0).getInnerText());
         }
      }
      return true;
   }

   public Set<XmlItemType> getAllowedItem()
   {
      return allowed;
   }

}
