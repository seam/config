/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.test.stereotype;

import org.jboss.seam.xml.test.AbstractXMLTest;
import org.jboss.seam.xml.test.interceptor.InterceptedBean;
import org.jboss.weld.environment.se.util.WeldManagerUtils;
import org.testng.annotations.Test;

/**
 * this is the same as the interceptor test except the interceptor is applied
 * through a stereotype
 */
public class StereotypeTest extends AbstractXMLTest
{

   @Override
   protected String getXmlFileName()
   {
      return "stereotype-beans.xml";
   }

   @Test(enabled = true)
   public void testStereotypes()
   {

      InterceptedBean x = WeldManagerUtils.getInstanceByType(manager, InterceptedBean.class);
      String res = x.method();
      assert res.equals("hello world");

   }
}
