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
import java.lang.reflect.Type;

/**
 * Class that knows how to set a field value, either directly or by calling a
 * getter
 * 
 * @author Stuart Douglas <stuart@baileyroberts.com.au>
 * 
 */
public interface FieldValueSetter
{

   public void set(Object instance, Object value) throws IllegalAccessException, InvocationTargetException;

   public Class<?> getType();

   public Type getGenericType();

   public Class<?> getDeclaringClass();

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
