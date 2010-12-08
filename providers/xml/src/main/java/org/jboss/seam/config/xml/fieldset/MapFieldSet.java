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
package org.jboss.seam.config.xml.fieldset;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.BeanManager;

import org.jboss.seam.config.xml.model.EntryXmlItem;
import org.jboss.seam.config.xml.util.TypeReader;
import org.jboss.seam.solder.properties.Property;

/**
 * class responsible for setting the value of map properties.
 * 
 * 
 * @author Stuart Douglas <stuart@baileyroberts.com.au>
 * 
 */
public class MapFieldSet implements FieldValueObject
{
   private final Property<Object> field;
   private final List<Entry<FieldValue, FieldValue>> values;
   private final Class<?> keyType;
   private final Class<?> valueType;
   private final Class<? extends Map> collectionType;

   public MapFieldSet(Property<Object> field, List<EntryXmlItem> items)
   {
      this.field = field;
      this.values = new ArrayList<Entry<FieldValue, FieldValue>>();
      // figure out the collection type
      Type type = field.getBaseType();
      if (type instanceof ParameterizedType)
      {
         ParameterizedType parameterizedType = (ParameterizedType) type;

         if (parameterizedType.getRawType() == Map.class)
         {
            collectionType = LinkedHashMap.class;
         }
         else if (parameterizedType.getRawType() == LinkedHashMap.class)
         {
            collectionType = LinkedHashMap.class;
         }
         else if (parameterizedType.getRawType() == HashMap.class)
         {
            collectionType = HashMap.class;
         }
         else if (parameterizedType.getRawType() == SortedMap.class)
         {
            collectionType = TreeMap.class;
         }
         else if (parameterizedType.getRawType() == TreeMap.class)
         {
            collectionType = TreeMap.class;
         }
         else
         {
            throw new RuntimeException("Could not determine element type for map " + field.getDeclaringClass().getName() + "." + field.getName());
         }

         keyType = TypeReader.readClassFromType(parameterizedType.getActualTypeArguments()[0]);
         valueType = TypeReader.readClassFromType(parameterizedType.getActualTypeArguments()[1]);
      }
      else
      {
         throw new RuntimeException("Could not determine element type for map " + field.getDeclaringClass().getName() + "." + field.getName());
      }

      for (EntryXmlItem i : items)
      {
         values.add(new EntryImpl(i.getKey().getValue(), i.getValue().getValue()));
      }
   }

   public void setValue(Object instance, CreationalContext<?> ctx, BeanManager manager)
   {
      try
      {
         Map res = collectionType.newInstance();
         field.setValue(instance, res);
         for (int i = 0; i < values.size(); ++i)
         {
            Entry<FieldValue, FieldValue> e = values.get(i);
            res.put(e.getKey().value(keyType, ctx, manager), e.getValue().value(valueType, ctx, manager));
         }
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }

   private final class EntryImpl implements Entry<FieldValue, FieldValue>
   {
      private FieldValue key;
      private FieldValue value;

      public EntryImpl(FieldValue key, FieldValue value)
      {
         this.key = key;
         this.value = value;
      }

      public FieldValue getKey()
      {
         return key;
      }

      public FieldValue getValue()
      {
         return value;
      }

      public FieldValue setValue(FieldValue value)
      {
         return this.value = value;
      }

      public void setKey(FieldValue key)
      {
         this.key = key;
      }
   }
}
