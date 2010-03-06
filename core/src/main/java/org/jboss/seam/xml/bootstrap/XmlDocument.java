/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.bootstrap;

import org.xml.sax.InputSource;

public interface XmlDocument
{
   public InputSource getInputSource();

   public String getFileUrl();
}
