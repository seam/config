/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.test.injection;

import org.jboss.seam.xml.test.AbstractXMLTest;
import org.testng.annotations.Test;

public class ProducerFieldValueBeanTest extends AbstractXMLTest
{

   @Override
   protected String getXmlFileName()
   {
      return "producer-field-value-beans.xml";
   }

   @Test
   public void testProducerField()
   {

      RecieverBean s = getReference(RecieverBean.class);
      assert s.value.equals("hello world");
   }

}
