/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.parser.namespace;

import org.jboss.seam.xml.model.XmlItem;
import org.jboss.seam.xml.parser.SaxNode;

public interface NamespaceElementResolver
{

   XmlItem getItemForNamespace(SaxNode node, XmlItem parent);

}
