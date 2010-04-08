/*
 * Distributed under the LGPL License
 * 
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

import org.jboss.seam.xml.model.EntryXmlItem;
import org.jboss.seam.xml.util.XmlObjectConverter;

/**
 * class responsible for setting the value of map properties.
 * 
 * 
 * @author Stuart Douglas <stuart@baileyroberts.com.au>
 * 
 */
public class MapFieldSet implements FieldValueObject
{
   FieldValueSetter field;
   List<MFS> values;
   Class<?> keyType;
   Class<?> valueType;
   Class<? extends Map> collectionType;

   public MapFieldSet(FieldValueSetter field, List<EntryXmlItem> items)
   {
      this.field = field;
      this.values = new ArrayList<MFS>();
      discoverElementType();

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
      Type type = field.getGenericType();
      if (type instanceof ParameterizedType)
      {
         ParameterizedType parameterizedType = (ParameterizedType) type;

         if (parameterizedType.getRawType() == Map.class)
         {
            keyType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
            valueType = (Class<?>) parameterizedType.getActualTypeArguments()[1];
            collectionType = LinkedHashMap.class;
         }
         else if (parameterizedType.getRawType() == LinkedHashMap.class)
         {
            keyType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
            valueType = (Class<?>) parameterizedType.getActualTypeArguments()[1];
            collectionType = LinkedHashMap.class;
         }
         else if (parameterizedType.getRawType() == HashMap.class)
         {
            keyType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
            valueType = (Class<?>) parameterizedType.getActualTypeArguments()[1];
            collectionType = HashMap.class;
         }
         else if (parameterizedType.getRawType() == SortedMap.class)
         {
            keyType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
            valueType = (Class<?>) parameterizedType.getActualTypeArguments()[1];
            collectionType = TreeMap.class;
         }
         else if (parameterizedType.getRawType() == TreeMap.class)
         {
            keyType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
            valueType = (Class<?>) parameterizedType.getActualTypeArguments()[1];
            collectionType = TreeMap.class;
         }
         else
         {
            throw new RuntimeException("Could not determine element type for map " + field.getDeclaringClass().getName() + "." + field.getName());
         }

      }
      else
      {
         throw new RuntimeException("Could not determine element type for map " + field.getDeclaringClass().getName() + "." + field.getName());
      }

   }

   public void setValue(Object instance)
   {
      try
      {
         Map res = collectionType.newInstance();
         field.set(instance, res);
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
