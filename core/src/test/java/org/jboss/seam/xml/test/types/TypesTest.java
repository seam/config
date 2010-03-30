/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.test.types;

import org.jboss.seam.xml.test.AbstractXMLTest;
import org.jboss.weld.environment.se.util.WeldManagerUtils;
import org.testng.annotations.Test;

/**
 * tests that <types> restricts the allowed types of an injection point
 */
public class TypesTest extends AbstractXMLTest
{

   @Override
   protected String getXmlFileName()
   {
      return "types-test-beans.xml";
   }

   @Test(enabled = true)
   public void testTypeRestriction()
   {

      TypeInjectedClass x = WeldManagerUtils.getInstanceByType(manager, TypeInjectedClass.class);
      assert x.value instanceof AllowedType;

   }
}
