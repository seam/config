/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.core;

import org.jboss.weld.extensions.annotated.AnnotatedTypeBuilder;

public class BeanResult<X>
{
   AnnotatedTypeBuilder<X> builder;
   Class<X> type;
   BeanResultType beanType = BeanResultType.ADD;

   public BeanResult(Class<X> type, boolean readAnnotations)
   {
      this.type = type;
      builder = AnnotatedTypeBuilder.newInstance(type);
      if (readAnnotations)
      {
         builder.readAnnotationsFromUnderlyingType();
      }
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

   public void setBeanType(BeanResultType beanType)
   {
      this.beanType = beanType;
   }

}
