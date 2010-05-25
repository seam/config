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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.xml.model.XmlItem;
import org.jboss.seam.xml.util.XmlObjectConverter;

/**
 * class responsible for setting the value of array properties.
 * 
 * @author Stuart Douglas <stuart@baileyroberts.com.au>
 * 
 */
public class ArrayFieldSet implements FieldValueObject
{
   FieldValueSetter field;
   List<AFS> values;
   Class arrayType;

   public ArrayFieldSet(FieldValueSetter field, List<XmlItem> items)
   {
      this.field = field;
      this.values = new ArrayList<AFS>();

      arrayType = field.getType().getComponentType();
      AFS setter;
      for (XmlItem i : items)
      {
         final Object fv = XmlObjectConverter.convert(arrayType, i.getInnerText());
         if (field.getType() == char.class)
         {

            final char val = (Character) fv;
            setter = new AFS()
            {
               public void set(Object o, int i) throws IllegalAccessException
               {
                  Array.setChar(o, i, val);
               }
            };
         }
         else if (field.getType() == int.class)
         {
            final int val = (Integer) fv;
            setter = new AFS()
            {
               public void set(Object o, int i) throws IllegalAccessException
               {
                  Array.setInt(o, i, val);
               }
            };
         }
         else if (field.getType() == short.class)
         {
            final short val = (Short) fv;
            setter = new AFS()
            {
               public void set(Object o, int i) throws IllegalAccessException
               {
                  Array.setShort(o, i, val);
               }
            };
         }
         else if (field.getType() == long.class)
         {
            final long val = (Long) fv;
            setter = new AFS()
            {
               public void set(Object o, int i) throws IllegalAccessException
               {
                  Array.setLong(o, i, val);
               }
            };
         }
         else if (field.getType() == byte.class)
         {
            final byte val = (Byte) fv;
            setter = new AFS()
            {
               public void set(Object o, int i) throws IllegalAccessException
               {
                  Array.setByte(o, i, val);
               }
            };
         }
         else if (field.getType() == double.class)
         {
            final double val = (Double) fv;
            setter = new AFS()
            {
               public void set(Object o, int i) throws IllegalAccessException
               {
                  Array.setDouble(o, i, val);
               }
            };
         }
         else if (field.getType() == float.class)
         {
            final float val = (Float) fv;
            setter = new AFS()
            {
               public void set(Object o, int i) throws IllegalAccessException
               {
                  Array.setFloat(o, i, val);
               }
            };
         }
         else
         {
            final Object val = fv;
            setter = new AFS()
            {
               public void set(Object o, int i) throws IllegalAccessException
               {
                  Array.set(o, i, val);
               }
            };
         }
         values.add(setter);
      }

   }

   public void setValue(Object instance)
   {
      try
      {
         Object array = Array.newInstance(arrayType, values.size());
         field.set(instance, array);
         for (int i = 0; i < values.size(); ++i)
         {
            values.get(i).set(array, i);
         }
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }

   interface AFS
   {
      void set(Object o, int i) throws IllegalAccessException;
   }

}
