/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.test.method;

import javax.enterprise.util.AnnotationLiteral;

import org.jboss.seam.xml.test.AbstractXMLTest;
import org.testng.annotations.Test;

public class PrimitiveMethodTest extends AbstractXMLTest
{

   @Override
   protected String getXmlFileName()
   {
      return "primitive-method-beans.xml";
   }

   @Test
   public void methodTest()
   {
      int x = getReference(int.class, new Qual1Lit());
      assert x == 1;
      int[] y = getReference(int[].class, new Qual2Lit());
      for (int i = 0; i < y.length; ++i)
      {
         assert y[i] == i + 2;
      }

   }

   class Qual1Lit extends AnnotationLiteral<Qualifier1>
   {
   };

   class Qual2Lit extends AnnotationLiteral<Qualifier2>
   {
   };

}
