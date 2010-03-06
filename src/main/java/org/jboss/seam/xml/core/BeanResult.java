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
   boolean override, extend;

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

   public boolean isOverride()
   {
      return override;
   }

   public void setOverride(boolean override)
   {
      this.override = override;
   }

   public boolean isExtend()
   {
      return extend;
   }

   public void setExtend(boolean extend)
   {
      this.extend = extend;
   }
}
