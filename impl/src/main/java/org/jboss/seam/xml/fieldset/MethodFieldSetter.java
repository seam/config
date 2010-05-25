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
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * class that sets a field value by calling a setter method
 * 
 * @author Stuart Douglas <stuart@baileyroberts.com.au>
 * 
 */
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
