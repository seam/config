package org.jboss.seam.xml.test.generic;

import javax.inject.Inject;

public class GenericDependant
{
   @Inject
   GenericMain instance;

   public int getValue()
   {
      return instance.getConfiguredValue() + 1;
   }
}
