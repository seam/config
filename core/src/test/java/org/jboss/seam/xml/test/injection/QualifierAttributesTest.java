/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.test.injection;

import org.jboss.seam.xml.test.AbstractXMLTest;
import org.jboss.weld.environment.se.util.WeldManagerUtils;
import org.testng.annotations.Test;

/**
 * Test that XML configured Qualifiers work as expected
 */
public class QualifierAttributesTest extends AbstractXMLTest
{

   @Override
   protected String getXmlFileName()
   {
      return "qualifier-attributes-test-beans.xml";
   }

   @Test()
   public void tstQualifiersWithAttributes()
   {
      QualifierTestBean x = WeldManagerUtils.getInstanceByType(manager, QualifierTestBean.class);
      assert x.bean1.getBeanNumber() == 1;
      assert x.bean2.getBeanNumber() == 2;

   }
}
