package org.jboss.seam.xml.fieldset;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

public interface FieldValueSetter
{

   public void set(Object instance, Object value) throws IllegalAccessException, InvocationTargetException;

   public Class getType();

   public Type getGenericType();

   public Class getDeclaringClass();

   public String getName();

   public void setInt(Object instance, int value) throws IllegalAccessException, InvocationTargetException;

   public void setShort(Object instance, short value) throws IllegalAccessException, InvocationTargetException;

   public void setLong(Object instance, long value) throws IllegalAccessException, InvocationTargetException;

   public void setByte(Object instance, byte value) throws IllegalAccessException, InvocationTargetException;

   public void setChar(Object instance, char value) throws IllegalAccessException, InvocationTargetException;

   public void setDouble(Object instance, double value) throws IllegalAccessException, InvocationTargetException;

   public void setFloat(Object instance, float value) throws IllegalAccessException, InvocationTargetException;

   public void setBoolean(Object instance, boolean value) throws IllegalAccessException, InvocationTargetException;

}
