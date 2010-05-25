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

import java.lang.reflect.InvocationTargetException;

import org.jboss.seam.xml.util.XmlObjectConverter;

public class SimpleFieldValue implements FieldValueObject
{

   final FieldValueSetter field;

   FS setter;

   public SimpleFieldValue(Class<?> javaObject, final FieldValueSetter f, final String value)
   {
      this.field = f;

      Object fv = XmlObjectConverter.convert(f.getType(), value);
      if (field.getType() == char.class)
      {

         final char val = (Character) fv;
         setter = new FS()
         {
            public void set(Object o) throws IllegalAccessException, InvocationTargetException
            {
               field.setChar(o, val);
            }
         };
      }
      else if (field.getType() == int.class)
      {
         final int val = (Integer) fv;
         setter = new FS()
         {
            public void set(Object o) throws IllegalAccessException, InvocationTargetException
            {
               field.setInt(o, val);
            }
         };
      }
      else if (field.getType() == short.class)
      {
         final short val = (Short) fv;
         setter = new FS()
         {
            public void set(Object o) throws IllegalAccessException, InvocationTargetException
            {
               field.setShort(o, val);
            }
         };
      }
      else if (field.getType() == long.class)
      {
         final long val = (Long) fv;
         setter = new FS()
         {
            public void set(Object o) throws IllegalAccessException, InvocationTargetException
            {
               field.setLong(o, val);
            }
         };
      }
      else if (field.getType() == byte.class)
      {
         final byte val = (Byte) fv;
         setter = new FS()
         {
            public void set(Object o) throws IllegalAccessException, InvocationTargetException
            {
               field.setByte(o, val);
            }
         };
      }
      else if (field.getType() == double.class)
      {
         final double val = (Double) fv;
         setter = new FS()
         {
            public void set(Object o) throws IllegalAccessException, InvocationTargetException
            {
               field.setDouble(o, val);
            }
         };
      }
      else if (field.getType() == float.class)
      {
         final float val = (Float) fv;
         setter = new FS()
         {
            public void set(Object o) throws IllegalAccessException, InvocationTargetException
            {
               field.setFloat(o, val);
            }
         };
      }
      else if (field.getType() == boolean.class)
      {
         final boolean val = (Boolean) fv;
         setter = new FS()
         {
            public void set(Object o) throws IllegalAccessException, InvocationTargetException
            {
               field.setBoolean(o, val);
            }
         };
      }
      else
      {
         final Object val = fv;
         setter = new FS()
         {
            public void set(Object o) throws IllegalAccessException, InvocationTargetException
            {
               field.set(o, val);
            }
         };
      }
   }

   interface FS
   {
      void set(Object o) throws IllegalAccessException, InvocationTargetException;
   }

   public void setValue(Object instance)
   {
      try
      {
         setter.set(instance);
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }

}
