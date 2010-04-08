package org.jboss.seam.xml.test.method;

import javax.enterprise.inject.Produces;

public class MethodValueArrayProducer
{
   @Produces
   @Qualifier2
   public MethodValueBean[][] createMethodValueBeans()
   {
      return new MethodValueBean[10][10];
   }
}
