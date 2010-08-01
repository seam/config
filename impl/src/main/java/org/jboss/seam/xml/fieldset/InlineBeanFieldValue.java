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

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.jboss.weld.extensions.util.properties.Property;

/**
 * Field value object for an inline bean definition
 * 
 * @author Stuart Douglas
 * 
 */
public class InlineBeanFieldValue implements FieldValueObject
{

   private final Property field;

   private final int beanId;

   private final Class<?> type;

   private final InlineBeanQualifier.InlineBeanQualifierLiteral literal;

   private final BeanManager manager;

   private Bean<?> bean;

   public InlineBeanFieldValue(Class<?> type, int syntheticBeanQualifierNo, Property setter, BeanManager manager)
   {
      this.beanId = syntheticBeanQualifierNo;
      this.field = setter;
      this.type = type;
      this.literal = new InlineBeanQualifier.InlineBeanQualifierLiteral(beanId);
      this.manager = manager;
   }

   public void setValue(Object instance, CreationalContext<?> ctx)
   {
      if (bean == null)
      {
         bean = manager.resolve(manager.getBeans(type, literal));
      }
      Object obj = manager.getReference(bean, type, ctx);
      try
      {
         field.setValue(instance, obj);
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }

}
