/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.test.simple;

import org.jboss.seam.xml.test.AbstractXMLTest;
import org.testng.annotations.Test;

public class VetoTest extends AbstractXMLTest
{

   @Override
   protected String getXmlFileName()
   {
      return "veto-beans.xml";
   }

   @Test
   public void testInterceptors()
   {

      try
      {
         VetoedBean y = getReference(VetoedBean.class);
         assert y != null : "Vetoed bean was installed";
      }
      catch (Exception e)
      {
         // we expect this to throw an exception instead of returning null
      }

   }
}
