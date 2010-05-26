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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

/**
 * class that can set a field value directly
 * 
 * @author Stuart Douglas <stuart@baileyroberts.com.au>
 * 
 */
public class DirectFieldSetter implements FieldValueSetter
{

   private final Field field;

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
