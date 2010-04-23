package org.jboss.seam.xml.test.generic;

import javax.inject.Inject;

public class GenericDependant
{
   @Inject
   GenericMain instance;

   boolean initCalled = false;

   @Inject
   public void init()
   {
      initCalled = true;
   }

   public int getValue()
   {
      return instance.getConfiguredValue() + 1;
   }
}
