/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.test.interceptor;

import org.jboss.seam.xml.test.AbstractXMLTest;
import org.testng.annotations.Test;

public class InterceptorTest extends AbstractXMLTest
{

   @Override
   protected String getXmlFileName()
   {
      return "interceptor-beans.xml";
   }

   @Test()
   public void testInterceptors()
   {

      InterceptedBean x = getReference(InterceptedBean.class);
      String res = x.method();
      assert res.equals("hello world");

   }
}
