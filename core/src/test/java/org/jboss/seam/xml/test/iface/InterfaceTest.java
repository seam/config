package org.jboss.seam.xml.test.iface;

import javax.enterprise.util.AnnotationLiteral;

import org.jboss.seam.xml.test.AbstractXMLTest;
import org.testng.annotations.Test;

public class InterfaceTest extends AbstractXMLTest
{

   @Override
   protected String getXmlFileName()
   {
      return "interface-beans.xml";
   }

   @Test
   public void testInterfaceConfig()
   {
      InterfaceBean x = getReference(InterfaceBean.class, new AnnotationLiteral<InterfaceQualifier>()
      {
      });
      assert x.message.equals("hello world");
      String s = getReference(String.class, new AnnotationLiteral<InterfaceQualifier>()
      {
      });
      assert s.equals("hi world");
   }

}