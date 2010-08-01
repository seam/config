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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.spi.CreationalContext;

import org.jboss.seam.xml.model.ValueXmlItem;
import org.jboss.weld.extensions.util.properties.Property;

/**
 * class responsible for setting the value of array properties.
 * 
 * @author Stuart Douglas <stuart@baileyroberts.com.au>
 * 
 */
public class ArrayFieldSet implements FieldValueObject
{
   final private Property field;
   final private List<FieldValue> values;
   final private Class<?> arrayType;

   public ArrayFieldSet(Property<?> field, List<ValueXmlItem> items)
   {
      this.field = field;
      this.values = new ArrayList<FieldValue>();
      arrayType = field.getJavaClass().getComponentType();
      for (ValueXmlItem i : items)
      {
         values.add(i.getValue());
      }

   }

   public void setValue(Object instance, CreationalContext<?> ctx)
   {
      try
      {
         Object array = Array.newInstance(arrayType, values.size());
         field.setValue(instance, array);
         for (int i = 0; i < values.size(); ++i)
         {
            Array.set(array, i, values.get(i).value(arrayType, ctx));
         }
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }
}
