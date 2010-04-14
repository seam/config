/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.core;

import org.jboss.weld.extensions.util.annotated.NewAnnotatedTypeBuilder;

public class BeanResult<X>
{
   NewAnnotatedTypeBuilder<X> builder;
   Class<X> type;
   BeanResultType beanType = BeanResultType.ADD;

   public BeanResult(Class<X> type, boolean readAnnotations)
   {
      this.type = type;
      builder = new NewAnnotatedTypeBuilder<X>(type, readAnnotations);
   }

   public NewAnnotatedTypeBuilder<X> getBuilder()
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
