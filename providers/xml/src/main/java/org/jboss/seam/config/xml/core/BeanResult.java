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
package org.jboss.seam.config.xml.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.NormalScope;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Scope;

import org.jboss.seam.config.xml.fieldset.FieldValueObject;
import org.jboss.seam.solder.core.Veto;
import org.jboss.seam.solder.reflection.annotated.AnnotatedTypeBuilder;

public class BeanResult<X>
{
   private final AnnotatedTypeBuilder<X> builder;
   private final Class<X> type;
   private final BeanResultType beanType;
   private final List<FieldValueObject> fieldValues;
   private final List<BeanResult<?>> inlineBeans;

   private final BeanManager beanManager;

   public BeanResult(Class<X> type, boolean readAnnotations, BeanResultType beanType, List<FieldValueObject> fieldValues, List<BeanResult<?>> inlineBeans, BeanManager beanManager)
   {
      this.beanManager = beanManager;
      this.type = type;
      builder = new AnnotatedTypeBuilder<X>().setJavaClass(type);
      builder.addToClass(XmlConfiguredBeanLiteral.INSTANCE);
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

   public void addToClass(Annotation annotation)
   {
      // TODO: this should be done with the BeanManager one WELD-721 is resolved
      if (annotation.annotationType().isAnnotationPresent(Scope.class) || annotation.annotationType().isAnnotationPresent(NormalScope.class))
      {
         // if the user is adding a new scope we need to remove any existing
         // ones
         for (Annotation typeAnnotation : type.getAnnotations())
         {
            if (typeAnnotation.annotationType().isAnnotationPresent(Scope.class) || typeAnnotation.annotationType().isAnnotationPresent(NormalScope.class))
            {
               builder.removeFromClass(typeAnnotation.annotationType());
            }
         }
      }
      builder.addToClass(annotation);
   }

   public void addToField(Field field, Annotation annotation)
   {
      if (annotation.annotationType().isAnnotationPresent(Scope.class) || annotation.annotationType().isAnnotationPresent(NormalScope.class))
      {
         for (Annotation typeAnnotation : field.getAnnotations())
         {
            if (typeAnnotation.annotationType().isAnnotationPresent(Scope.class) || typeAnnotation.annotationType().isAnnotationPresent(NormalScope.class))
            {
               builder.removeFromField(field, typeAnnotation.annotationType());
            }
         }
      }
      builder.addToField(field, annotation);
   }

   public void addToMethod(Method method, Annotation annotation)
   {
      if (annotation.annotationType().isAnnotationPresent(Scope.class) || annotation.annotationType().isAnnotationPresent(NormalScope.class))
      {
         for (Annotation typeAnnotation : method.getAnnotations())
         {
            if (typeAnnotation.annotationType().isAnnotationPresent(Scope.class) || typeAnnotation.annotationType().isAnnotationPresent(NormalScope.class))
            {
               builder.removeFromMethod(method, typeAnnotation.annotationType());
            }
         }
      }
      builder.addToMethod(method, annotation);
   }

   public void addToMethodParameter(Method method, int param, Annotation annotation)
   {
      builder.addToMethodParameter(method, param, annotation);
   }

   public void addToConstructor(Constructor<?> constructor, Annotation annotation)
   {
      builder.addToConstructor((Constructor) constructor, annotation);
   }

   public void addToConstructorParameter(Constructor<?> constructor, int param, Annotation annotation)
   {
      builder.addToConstructorParameter((Constructor) constructor, param, annotation);
   }

   public void overrideFieldType(Field field, Class<?> javaClass)
   {
      builder.overrideFieldType(field, javaClass);
   }

   public AnnotatedType<?> getAnnotatedType()
   {
      return builder.create();
   }

}
