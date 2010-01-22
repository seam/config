package org.jboss.seam.xml.fieldset;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class MethodFieldSetter implements FieldValueSetter
{
   Method method;

   public MethodFieldSetter(Method method)
   {
      this.method = method;
   }

   public void set(Object instance, Object value) throws IllegalAccessException, InvocationTargetException
   {
      method.invoke(instance, value);
   }

   public Type getGenericType()
   {
      return method.getGenericParameterTypes()[0];
   }

   public String getName()
   {
      return method.getName();
   }

   public Class<?> getDeclaringClass()
   {
      return method.getDeclaringClass();
   }

   public Class<?> getType()
   {
      return method.getParameterTypes()[0];
   }

   public void setBoolean(Object instance, boolean value) throws IllegalAccessException, InvocationTargetException
   {
      set(instance, value);
   }

   public void setByte(Object instance, byte value) throws IllegalAccessException, InvocationTargetException
   {
      set(instance, value);
   }

   public void setChar(Object instance, char value) throws IllegalAccessException, InvocationTargetException
   {
      set(instance, value);
   }

   public void setDouble(Object instance, double value) throws IllegalAccessException, InvocationTargetException
   {
      set(instance, value);
   }

   public void setFloat(Object instance, float value) throws IllegalAccessException, InvocationTargetException
   {
      set(instance, value);
   }

   public void setInt(Object instance, int value) throws IllegalAccessException, InvocationTargetException
   {
      set(instance, value);
   }

   public void setLong(Object instance, long value) throws IllegalAccessException, InvocationTargetException
   {
      set(instance, value);
   }

   public void setShort(Object instance, short value) throws IllegalAccessException, InvocationTargetException
   {
      set(instance, value);
   }

}
