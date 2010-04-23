package org.jboss.seam.xml.test.generic;

import javax.inject.Inject;

public class GenericMain
{
   int configuredValue;

   boolean init = false;

   @Inject
   public void setup()
   {
      init = true;
   }

   public int getConfiguredValue()
   {
      return configuredValue;
   }

   public void setConfiguredValue(int configuredValue)
   {
      this.configuredValue = configuredValue;
   }

}
