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
package org.jboss.seam.xml.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.jboss.seam.xml.fieldset.DirectFieldSetter;
import org.jboss.seam.xml.fieldset.FieldValueSetter;
import org.jboss.seam.xml.fieldset.MethodFieldSetter;
import org.jboss.seam.xml.fieldset.SimpleFieldValue;
import org.jboss.seam.xml.util.TypeOccuranceInformation;

public class FieldXmlItem extends AbstractFieldXmlItem
{

   private final Field field;

   public FieldXmlItem(XmlItem parent, Field c, String innerText, String document, int lineno)
   {
      super(XmlItemType.FIELD, parent, parent.getJavaClass(), innerText, null, document, lineno);
      this.field = c;
      this.fieldSetter = getFieldValueSetter(c);
      if (innerText != null && innerText.length() > 0)
      {
         fieldValue = new SimpleFieldValue(parent.getJavaClass(), fieldSetter, innerText);
      }
      allowed.add(TypeOccuranceInformation.of(XmlItemType.ANNOTATION, null, null));
      allowed.add(TypeOccuranceInformation.of(XmlItemType.VALUE, null, null));
      allowed.add(TypeOccuranceInformation.of(XmlItemType.ENTRY, null, null));
   }

   public Field getField()
   {
      return field;
   }

   private FieldValueSetter getFieldValueSetter(Field field)
   {
      String fieldName = field.getName();
      String methodName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
      Method setter = null;
      try
      {
         setter = field.getDeclaringClass().getMethod(methodName, field.getType());
      }
      catch (SecurityException e)
      {

      }
      catch (NoSuchMethodException e)
      {

      }
      if (setter != null)
      {
         return new MethodFieldSetter(setter);
      }
      return new DirectFieldSetter(field);
   }

   @Override
   public Class<?> getDeclaringClass()
   {
      return field.getDeclaringClass();
   }

   @Override
   public String getFieldName()
   {
      return field.getName();
   }

   @Override
   public Class<?> getFieldType()
   {
      return field.getType();
   }
}
