/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.test.fieldset;

import org.jboss.seam.xml.test.AbstractXMLTest;
import org.jboss.weld.environment.se.util.WeldManagerUtils;
import org.testng.annotations.Test;

public class SetArrayFieldValueBeanTest extends AbstractXMLTest
{

   @Override
   protected String getXmlFileName()
   {
      return "array-set-field-value-beans.xml";
   }

   @Test
   public void arrayFieldSetterTest()
   {
      ArrayFieldValue x = WeldManagerUtils.getInstanceByType(manager, ArrayFieldValue.class);
      assert x.carray.length == 2;
      assert x.iarray.length == 2;
      assert x.sarray.length == 2;
      assert x.sarray[0].equals("hello");
      assert x.sarray[1].equals("world");
      assert x.iarray[0] == 1;
      assert x.iarray[1] == 2;
      assert x.carray[0] == Integer.class;
      assert x.carray[1] == Long.class;

   }

}
