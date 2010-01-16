/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.bootstrap;

/**
 * classes that implement this interface can be used to load other XML
 * documents, this is useful for testing
 * 
 * @author Stuart Douglas <stuart@baileyroberts.com.au>
 * 
 */
public interface XmlDocumentProvider
{

   void open();

   XmlDocument getNextDocument();

   void close();

}
