/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.parser.namespace;

import java.util.Map;

import org.jboss.seam.xml.model.XmlItem;

public interface NamespaceElementResolver
{

   XmlItem getItemForNamespace(String item, XmlItem parent, String innerText, Map<String, String> attributes) throws InvalidElementException;

}
