/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.test.method;

import org.jboss.seam.xml.test.AbstractXMLTest;
import org.testng.annotations.Test;

public class MethodTest extends AbstractXMLTest
{

   @Override
   protected String getXmlFileName()
   {
      return "method-beans.xml";
   }

   @Test
   public void methodTest()
   {
      MethodTarget x = getReference(MethodTarget.class);
      assert x != null;
      assert x.value1 == 1;
      assert x.value2 == 11;

   }

}
