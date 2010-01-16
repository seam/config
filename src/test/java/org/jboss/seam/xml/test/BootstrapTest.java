/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.test;

import java.util.List;

import org.jboss.seam.xml.bootstrap.XmlDocumentProvider;
import org.jboss.seam.xml.bootstrap.XmlExtension;
import org.testng.annotations.Test;

/**
 * Unit test for simple App.
 */
public class BootstrapTest extends AbstractXMLTest
{

   @Override
   protected String getXmlFileName()
   {
      return "single-bean.xml";
   }

   @Test
   public void testDocumentProviders()
   {
      XmlExtension x = getReference(XmlExtension.class);
      List<Class<? extends XmlDocumentProvider>> providers = x.getDocumentProviders();
      assert providers.size() == 2;

   }
}
