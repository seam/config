/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.test;

import org.jboss.seam.xml.bootstrap.ResourceLoaderXmlDocumentProvider;
import org.jboss.seam.xml.bootstrap.XmlDocument;
import org.jboss.seam.xml.bootstrap.XmlDocumentProvider;

public class TestXmlProvider implements XmlDocumentProvider
{

   public static String fileName;

   ResourceLoaderXmlDocumentProvider docProvider;

   public void close()
   {
      docProvider.close();
   }

   public XmlDocument getNextDocument()
   {
      return docProvider.getNextDocument();
   }

   public void open()
   {
      String[] r = new String[1];
      r[0] = fileName;
      docProvider = new ResourceLoaderXmlDocumentProvider(r);
      docProvider.open();
   }

}
