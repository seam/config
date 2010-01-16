/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.fieldset;

import java.lang.reflect.Field;

import org.jboss.seam.xml.util.XmlObjectConverter;

public class SimpleFieldValue implements FieldValueObject
{

   final Field field;

   FS setter;

   public SimpleFieldValue(Class javaObject, final Field f, final String value)
   {
      this.field = f;
      field.setAccessible(true);

      Object fv = XmlObjectConverter.convert(f.getType(), value);
      if (field.getType() == char.class)
      {

         final char val = (Character) fv;
         setter = new FS()
         {
            public void set(Object o) throws IllegalAccessException
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
            public void set(Object o) throws IllegalAccessException
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
            public void set(Object o) throws IllegalAccessException
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
            public void set(Object o) throws IllegalAccessException
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
            public void set(Object o) throws IllegalAccessException
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
            public void set(Object o) throws IllegalAccessException
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
            public void set(Object o) throws IllegalAccessException
            {
               field.setFloat(o, val);
            }
         };
      }
      else
      {
         final Object val = fv;
         setter = new FS()
         {
            public void set(Object o) throws IllegalAccessException
            {
               field.set(o, val);
            }
         };
      }
   }

   interface FS
   {
      void set(Object o) throws IllegalAccessException;
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
