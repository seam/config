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
package org.jboss.seam.xml.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jboss.seam.xml.fieldset.FieldValueObject;
import org.jboss.weld.extensions.annotated.AnnotatedTypeBuilder;
import org.jboss.weld.extensions.core.Veto;

public class BeanResult<X>
{
   private final AnnotatedTypeBuilder<X> builder;
   private final Class<X> type;
   private final BeanResultType beanType;
   private final List<FieldValueObject> fieldValues;
   private final List<BeanResult<?>> inlineBeans;

   public BeanResult(Class<X> type, boolean readAnnotations, BeanResultType beanType, List<FieldValueObject> fieldValues, List<BeanResult<?>> inlineBeans)
   {
      this.type = type;
      builder = new AnnotatedTypeBuilder().setJavaClass(type);
      if (readAnnotations)
      {
         builder.readFromType(type);
         // we don't want to keep the veto annotation on the class
         builder.removeFromClass(Veto.class);
      }
      this.beanType = beanType;
      this.fieldValues = new ArrayList<FieldValueObject>(fieldValues);
      this.inlineBeans = new ArrayList<BeanResult<?>>(inlineBeans);
   }

   public List<BeanResult<?>> getInlineBeans()
   {
      return inlineBeans;
   }

   public AnnotatedTypeBuilder<X> getBuilder()
   {
      return builder;
   }

   public Class<X> getType()
   {
      return type;
   }

   public BeanResultType getBeanType()
   {
      return beanType;
   }

   public List<FieldValueObject> getFieldValues()
   {
      return Collections.unmodifiableList(fieldValues);
   }

}
