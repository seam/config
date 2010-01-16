/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.test.simple;

import org.jboss.seam.xml.test.AbstractXMLTest;
import org.jboss.weld.environment.se.util.WeldManagerUtils;
import org.testng.annotations.Test;

/**
 * Unit test for simple App.
 */
public class SimpleBeanTest extends AbstractXMLTest
{

   @Override
   protected String getXmlFileName()
   {
      return "simple-beans.xml";
   }

   @Test
   public void simpleBeanTest()
   {
      Bean1 x = WeldManagerUtils.getInstanceByType(manager, Bean1.class);
      assert x != null;
      assert x.bean2 != null;

      Bean3 y = WeldManagerUtils.getInstanceByType(manager, Bean3.class);
      assert y != null;
      assert x.value == 1 : "Post construct method not called";
   }

}
