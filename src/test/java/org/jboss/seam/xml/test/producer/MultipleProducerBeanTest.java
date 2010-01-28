/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.test.producer;

import org.jboss.seam.xml.test.AbstractXMLTest;
import org.testng.annotations.Test;

public class MultipleProducerBeanTest extends AbstractXMLTest
{

   @Override
   protected String getXmlFileName()
   {
      return "multiple-producers.xml";
   }

   @Test
   public void testProducerField()
   {

      Reciever s = getReference(Reciever.class);
      assert s.val1==1;
      assert s.val2==2;
   }
   
   
   @Test
   public void testProducerMethod()
   {

      Reciever s = getReference(Reciever.class);
      assert s.meth1==1;
      assert s.meth2==2;
   }
}
