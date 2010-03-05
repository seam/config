package org.jboss.seam.xml.test.constructor;

import org.jboss.seam.xml.test.AbstractXMLTest;
import org.testng.annotations.Test;

public class ConstructorTest extends AbstractXMLTest
{
   @Override
   protected String getXmlFileName()
   {
      return "constructor-beans.xml";
   }

   @Test
   public void testBeanConstructorAnnotations()
   {
      ConstructedBean bean = getReference(ConstructedBean.class);
      assert bean.getValue() == 10;

   }
}
