/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.fieldset;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.jboss.seam.xml.model.XmlItem;
import org.jboss.seam.xml.util.XmlObjectConverter;

/**
 * class responsible for setting the value of collection properties.
 * 
 * It can deal with the following collection types: -Set -List -Collection
 * -SortedSet -HashSet -ArrayList -TreeSet -LinkedList
 * 
 * @author Stuart Douglas <stuart@baileyroberts.com.au>
 * 
 */
public class CollectionFieldSet implements FieldValueObject
{
   Field field;
   List<CFS> values;
   Class elementType;
   Class<? extends Collection> collectionType;

   public CollectionFieldSet(Field field, List<XmlItem> items)
   {
      this.field = field;
      this.values = new ArrayList<CFS>();
      discoverElementType();

      CFS setter;
      for (XmlItem i : items)
      {
         final Object fv = XmlObjectConverter.convert(elementType, i.getInnerText());

         setter = new CFS()
         {
            public void add(Collection o) throws IllegalAccessException
            {
               o.add(fv);
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

         if (parameterizedType.getRawType() == Collection.class)
         {
            elementType = (Class) parameterizedType.getActualTypeArguments()[0];
            collectionType = LinkedHashSet.class;
         }
         else if (parameterizedType.getRawType() == List.class)
         {
            elementType = (Class) parameterizedType.getActualTypeArguments()[0];
            collectionType = ArrayList.class;
         }
         else if (parameterizedType.getRawType() == Set.class)
         {
            elementType = (Class) parameterizedType.getActualTypeArguments()[0];
            collectionType = LinkedHashSet.class;
         }
         else if (parameterizedType.getRawType() == SortedSet.class)
         {
            elementType = (Class) parameterizedType.getActualTypeArguments()[0];
            collectionType = TreeSet.class;
         }
         else if (parameterizedType.getRawType() == HashSet.class)
         {
            elementType = (Class) parameterizedType.getActualTypeArguments()[0];
            collectionType = HashSet.class;
         }
         else if (parameterizedType.getRawType() == ArrayList.class)
         {
            elementType = (Class) parameterizedType.getActualTypeArguments()[0];
            collectionType = ArrayList.class;
         }
         else if (parameterizedType.getRawType() == LinkedList.class)
         {
            elementType = (Class) parameterizedType.getActualTypeArguments()[0];
            collectionType = LinkedList.class;
         }
         else if (parameterizedType.getRawType() == LinkedHashSet.class)
         {
            elementType = (Class) parameterizedType.getActualTypeArguments()[0];
            collectionType = LinkedHashSet.class;
         }
         else if (parameterizedType.getRawType() == TreeSet.class)
         {
            elementType = (Class) parameterizedType.getActualTypeArguments()[0];
            collectionType = TreeSet.class;
         }
         else
         {
            throw new RuntimeException("Could not determine element type for " + field.getDeclaringClass().getName() + "." + field.getName());
         }

      }
      else
      {
         throw new RuntimeException("Could not determine element type for " + field.getDeclaringClass().getName() + "." + field.getName());
      }

   }

   public void setValue(Object instance)
   {
      try
      {
         Collection res = collectionType.newInstance();
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

   interface CFS
   {
      void add(Collection o) throws IllegalAccessException;
   }

}
