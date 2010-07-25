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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.inject.spi.BeanManager;

import org.jboss.seam.xml.core.BeanResult;
import org.jboss.seam.xml.fieldset.ArrayFieldSet;
import org.jboss.seam.xml.fieldset.CollectionFieldSet;
import org.jboss.seam.xml.fieldset.FieldValueObject;
import org.jboss.seam.xml.fieldset.FieldValueSetter;
import org.jboss.seam.xml.fieldset.InlineBeanFieldValue;
import org.jboss.seam.xml.fieldset.MapFieldSet;
import org.jboss.seam.xml.fieldset.MethodFieldSetter;
import org.jboss.seam.xml.fieldset.SimpleFieldValue;
import org.jboss.seam.xml.util.TypeOccuranceInformation;
import org.jboss.seam.xml.util.XmlConfigurationException;

public class PropertyXmlItem extends AbstractXmlItem implements FieldValueXmlItem
{

   FieldValueSetter fieldSetter;
   FieldValueObject fieldValue;
   String name;
   Class<?> type;
   HashSet<TypeOccuranceInformation> allowed = new HashSet<TypeOccuranceInformation>();
   List<BeanResult<?>> inlineBeans = new ArrayList<BeanResult<?>>();

   public PropertyXmlItem(XmlItem parent, String name, Method setter, String innerText, String document, int lineno)
   {
      super(XmlItemType.FIELD, parent, parent.getJavaClass(), innerText, null, document, lineno);
      this.name = name;
      this.type = setter.getParameterTypes()[0];
      this.fieldSetter = new MethodFieldSetter(setter);
      if (innerText != null && innerText.length() > 0)
      {
         fieldValue = new SimpleFieldValue(parent.getJavaClass(), fieldSetter, innerText);
      }
      allowed.add(TypeOccuranceInformation.of(XmlItemType.VALUE, null, null));
      allowed.add(TypeOccuranceInformation.of(XmlItemType.ENTRY, null, null));
   }

   public FieldValueObject getFieldValue()
   {
      return fieldValue;
   }

   @Override
   public boolean resolveChildren(BeanManager manager)
   {
      List<EntryXmlItem> mapEntries = new ArrayList<EntryXmlItem>();
      List<ValueXmlItem> valueEntries = new ArrayList<ValueXmlItem>();
      if (fieldValue == null)
      {
         for (XmlItem i : children)
         {
            if (i.getType() == XmlItemType.VALUE)
            {
               valueEntries.add((ValueXmlItem) i);
            }
            else if (i.getType() == XmlItemType.ENTRY)
            {
               mapEntries.add((EntryXmlItem) i);
            }

         }
      }
      if (!mapEntries.isEmpty() || !valueEntries.isEmpty())
      {
         if (Map.class.isAssignableFrom(type))
         {
            if (!valueEntries.isEmpty())
            {
               throw new XmlConfigurationException("Map fields cannot have <value> elements as children,only <entry> elements Field:" + parent.getJavaClass().getName() + '.' + name, getDocument(), getLineno());
            }
            if (!mapEntries.isEmpty())
            {
               fieldValue = new MapFieldSet(fieldSetter, mapEntries);
            }
         }
         else if (Collection.class.isAssignableFrom(type) || type.isArray())
         {
            if (!mapEntries.isEmpty())
            {
               throw new XmlConfigurationException("Collection fields must be set using <value> not <entry> Field:" + parent.getJavaClass().getName() + '.' + name, getDocument(), getLineno());
            }
            if (!valueEntries.isEmpty())
            {
               if (type.isArray())
               {
                  fieldValue = new ArrayFieldSet(fieldSetter, valueEntries);
               }
               else
               {
                  fieldValue = new CollectionFieldSet(fieldSetter, valueEntries);
               }
            }
         }
         else
         {
            if (!mapEntries.isEmpty())
            {
               throw new XmlConfigurationException("Only Map fields can be set using <entry> Field:" + parent.getJavaClass().getName() + '.' + name, getDocument(), getLineno());
            }
            if (valueEntries.size() != 1)
            {
               throw new XmlConfigurationException("Non collection fields can only have a single <value> element Field:" + parent.getJavaClass().getName() + '.' + name, getDocument(), getLineno());
            }
            ValueXmlItem value = valueEntries.get(0);
            BeanResult<?> result = value.getBeanResult(manager);
            if (result == null)
            {
               fieldValue = new SimpleFieldValue(parent.getJavaClass(), fieldSetter, valueEntries.get(0).getInnerText());
            }
            else
            {
               inlineBeans.add(result);
               fieldValue = new InlineBeanFieldValue(type, value.getSyntheticQualifierId(), fieldSetter, manager);
            }
         }
      }
      return true;
   }

   public Set<TypeOccuranceInformation> getAllowedItem()
   {
      return allowed;
   }

   public String getFieldName()
   {
      return name;
   }

   public Collection<? extends BeanResult> getInlineBeans()
   {
      return inlineBeans;
   }
}
