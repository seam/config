package org.jboss.seam.xml.bootstrap;

import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

public class XmlProcessAnnotatedType implements ProcessAnnotatedType
{
   final AnnotatedType annotatedType;

   XmlProcessAnnotatedType(AnnotatedType annotatedType)
   {
      this.annotatedType = annotatedType;
   }

   public AnnotatedType getAnnotatedType()
   {
      return annotatedType;
   }

   public void setAnnotatedType(AnnotatedType type)
   {
      // nop for now
   }

   public void veto()
   {
      // nop for now
   }
}
