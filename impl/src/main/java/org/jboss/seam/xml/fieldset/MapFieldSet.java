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
package org.jboss.seam.xml.fieldset;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.enterprise.context.spi.CreationalContext;

import org.jboss.seam.xml.model.EntryXmlItem;
import org.jboss.seam.xml.util.TypeReader;
import org.jboss.seam.xml.util.XmlObjectConverter;
import org.jboss.weld.extensions.util.properties.Property;

/**
 * class responsible for setting the value of map properties.
 * 
 * 
 * @author Stuart Douglas <stuart@baileyroberts.com.au>
 * 
 */
public class MapFieldSet implements FieldValueObject
{
   private final Property field;
   private final List<MFS> values;
   private final Class<?> keyType;
   private final Class<?> valueType;
   private final Class<? extends Map> collectionType;

   public MapFieldSet(Property field, List<EntryXmlItem> items)
   {
      this.field = field;
      this.values = new ArrayList<MFS>();
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
         MFS setter;
         final Object key = XmlObjectConverter.convert(keyType, i.getKey().getInnerText());
         final Object value = XmlObjectConverter.convert(valueType, i.getValue().getInnerText());
         setter = new MFS()
         {
            @SuppressWarnings("unchecked")
            public void add(Map m) throws IllegalAccessException
            {
               m.put(key, value);
            }
         };

         values.add(setter);
      }

   }

   public void discoverElementType()
   {

   }

   public void setValue(Object instance, CreationalContext<?> ctx)
   {
      try
      {
         Map res = collectionType.newInstance();
         field.setValue(instance, res);
         for (int i = 0; i < values.size(); ++i)
         {
            values.get(i).add(res);
         }
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }

   interface MFS
   {
      void add(Map<?, ?> o) throws IllegalAccessException;
   }

}
