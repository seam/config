package org.jboss.seam.config.xml.bootstrap;

import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

public class ProcessAnnotatedTypeImpl implements ProcessAnnotatedType<Object>
{

   private AnnotatedType<Object> type;

   public ProcessAnnotatedTypeImpl(AnnotatedType<Object> type)
   {
      this.type = type;
   }

   @Override
   public AnnotatedType<Object> getAnnotatedType()
   {
      return type;
   }

   @Override
   public void setAnnotatedType(AnnotatedType<Object> type)
   {
      this.type = type;
   }

   @Override
   public void veto()
   {
      // ignore
   }

}
