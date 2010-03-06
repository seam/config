/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.test.fieldset;

import org.jboss.seam.xml.test.AbstractXMLTest;
import org.jboss.weld.environment.se.util.WeldManagerUtils;
import org.testng.annotations.Test;

public class SetMapFieldValueBeanTest extends AbstractXMLTest
{

   @Override
   protected String getXmlFileName()
   {
      return "map-set-field-value-beans.xml";
   }

   @Test
   public void mapSetFieldValue()
   {
      MapFieldValue x = WeldManagerUtils.getInstanceByType(manager, MapFieldValue.class);
      assert x.map1.size() == 2;
      assert x.map2.size() == 2;

      assert x.map1.get(1).equals("hello");
      assert x.map1.get(2).equals("world");
      assert x.map2.get("1") == Integer.class;
      assert x.map2.get("2") == Long.class;

   }
}
