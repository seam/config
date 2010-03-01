package org.jboss.seam.xml.test.dependency;

import org.jboss.seam.xml.test.AbstractXMLTest;
import org.testng.annotations.Test;

public class DependencyTest extends AbstractXMLTest
{

   @Override
   protected String getXmlFileName()
   {
      return "dependency-beans.xml";
   }

   @Test
   public void testDependencies()
   {
      SimpleTimer timer = getReference(SimpleTimer.class);
      assert timer.getType().equals("simple");
   }
}
