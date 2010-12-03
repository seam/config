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
package org.jboss.seam.config.xml.fieldset;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.BeanManager;

import org.jboss.seam.config.xml.util.XmlObjectConverter;

/**
 * Represents a simple field value in an XML document
 * 
 * @author Stuart Douglas
 * 
 */
public class ConstantFieldValue implements FieldValue
{
   private final String stringValue;

   public ConstantFieldValue(String stringValue)
   {
      this.stringValue = stringValue;
   }

   public Object value(Class<?> type, CreationalContext<?> cyx, BeanManager manager)
   {
      return XmlObjectConverter.convert(type, stringValue);
   }
}
