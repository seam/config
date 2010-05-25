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
package org.jboss.seam.xml.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;

/**
 * utility class that can convert a string representation of a type into an
 * instance of that type
 * 
 * @author Stuart Douglas <stuart@baileyroberts.com.au>
 * 
 */
public class XmlObjectConverter
{
   public static Object convert(Class target, String value)
   {
      if (target == String.class)
      {
         return value;
      }
      else if (target.isEnum())
      {
         return Enum.valueOf((Class<Enum>) target, value);
      }
      else if (target == Class.class)
      {
         try
         {
            return XmlObjectConverter.class.getClassLoader().loadClass(value);
         }
         catch (ClassNotFoundException e)
         {
            try
            {
               return Thread.currentThread().getContextClassLoader().loadClass(value);
            }
            catch (ClassNotFoundException e1)
            {
               throw new RuntimeException("Could not set field value to class configured in XML: " + value, e1);
            }
         }
      }
      // Date and time
      else if (java.sql.Date.class == target)
      {
         try
         {
            java.util.Date dval = DateFormat.getDateTimeInstance().parse(value);
            return new java.sql.Date(dval.getYear(), dval.getMonth(), dval.getDay());
         }
         catch (ParseException e)
         {
            throw new RuntimeException("Cannot parse javax.sql.Date field value: " + value, e);
         }
      }
      else if (java.sql.Time.class == target)
      {
         try
         {
            java.util.Date dval = DateFormat.getDateTimeInstance().parse(value);
            return new java.sql.Time(dval.getHours(), dval.getMinutes(), dval.getSeconds());
         }
         catch (ParseException e)
         {
            throw new RuntimeException("Cannot parse javax.sql.Date field value: " + value, e);
         }
      }
      else if (java.sql.Timestamp.class == target)
      {
         try
         {
            java.util.Date dval = DateFormat.getDateTimeInstance().parse(value);
            return new java.sql.Timestamp(dval.getYear(), dval.getMonth(), dval.getDay(), dval.getHours(), dval.getMinutes(), dval.getSeconds(), 0);
         }
         catch (ParseException e)
         {
            throw new RuntimeException("Cannot parse javax.sql.Date field value: " + value, e);
         }

      }
      else if (Calendar.class == target)
      {
         try
         {
            java.util.Date dval = DateFormat.getDateTimeInstance().parse(value);
            final Calendar val = Calendar.getInstance();
            val.setTime(dval);
            return val;
         }
         catch (ParseException e)
         {
            throw new RuntimeException("Cannot parse Calendar field value: " + value, e);
         }
      }
      else if (java.util.Date.class == target)
      {
         try
         {
            return DateFormat.getDateTimeInstance().parse(value);
         }
         catch (ParseException e)
         {
            throw new RuntimeException("Cannot parse Calendar field value: " + value, e);
         }
      }
      else if (target == BigDecimal.class)
      {
         return new BigDecimal(value);
      }
      else if (target == BigInteger.class)
      {
         return new BigInteger(value);
      }
      // primitive types
      else if (target == char.class || target == Character.class)
      {
         if (value.length() != 1)
         {
            throw new RuntimeException("Value of a char field must be exactly 1 character long");
         }
         return new Character(value.charAt(0));
      }
      else if (target == int.class || target == Integer.class)
      {
         return Integer.parseInt(value);
      }
      else if (target == short.class || target == Short.class)
      {
         return new Short(value);
      }
      else if (target == long.class || target == Long.class)
      {
         return new Long(value);
      }
      else if (target == byte.class || target == byte.class)
      {
         return new Byte(value);
      }
      else if (target == double.class || target == Double.class)
      {
         return new Double(value);
      }
      else if (target == float.class || target == Float.class)
      {
         return new Float(value);
      }
      else if (target == boolean.class || target == Boolean.class)
      {
         return new Boolean(value);
      }
      throw new RuntimeException("Could not convert value " + value + " to " + target.getName());
   }
}
