/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.test.generic;

import javax.enterprise.util.AnnotationLiteral;

import org.jboss.seam.xml.test.AbstractXMLTest;
import org.testng.annotations.Test;

public class GenericBeanTest extends AbstractXMLTest
{

   @Override
   protected String getXmlFileName()
   {
      return "generic-beans.xml";
   }

   @Test
   public void genericBeansTest()
   {
      GenericDependant dep = getReference(GenericDependant.class, new AnnotationLiteral<HighGenericQualifier>()
      {
      });
      // assert dep.getValue() == 101 : " actual " + dep.getValue();
      dep = getReference(GenericDependant.class, new AnnotationLiteral<LowGenericQualifier>()
      {
      });
      assert dep.getValue() == 11 : " actual " + dep.getValue();
      ;
   }

}
