package org.jboss.seam.xml.fieldset;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

public class DirectFieldSetter implements FieldValueSetter
{

   Field field;

   public DirectFieldSetter(Field field)
   {
      this.field = field;
      field.setAccessible(true);
   }

   public void set(Object instance, Object value) throws IllegalAccessException
   {
      field.set(instance, value);
   }

   public Type getGenericType()
   {
      return field.getGenericType();
   }

   public Class<?> getType()
   {
      return field.getType();
   }

   public String getName()
   {
      return field.getName();
   }

   public Class<?> getDeclaringClass()
   {
      return field.getDeclaringClass();
   }

   public void setBoolean(Object instance, boolean value) throws IllegalAccessException, InvocationTargetException
   {
      field.setBoolean(instance, value);
   }

   public void setByte(Object instance, byte value) throws IllegalAccessException, InvocationTargetException
   {
      field.setByte(instance, value);
   }

   public void setChar(Object instance, char value) throws IllegalAccessException, InvocationTargetException
   {
      field.setChar(instance, value);
   }

   public void setDouble(Object instance, double value) throws IllegalAccessException, InvocationTargetException
   {
      field.setDouble(instance, value);
   }

   public void setFloat(Object instance, float value) throws IllegalAccessException, InvocationTargetException
   {
      field.setFloat(instance, value);
   }

   public void setInt(Object instance, int value) throws IllegalAccessException, InvocationTargetException
   {
      field.setInt(instance, value);
   }

   public void setLong(Object instance, long value) throws IllegalAccessException, InvocationTargetException
   {
      field.setLong(instance, value);
   }

   public void setShort(Object instance, short value) throws IllegalAccessException, InvocationTargetException
   {
      field.setShort(instance, value);
   }

}
