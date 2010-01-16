/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.parser.namespace;

import java.util.Map;

import org.jboss.seam.xml.model.ArrayXmlItem;
import org.jboss.seam.xml.model.DependsXmlItem;
import org.jboss.seam.xml.model.EntryXmlItem;
import org.jboss.seam.xml.model.KeyXmlItem;
import org.jboss.seam.xml.model.ValueXmlItem;
import org.jboss.seam.xml.model.VetoXmlItem;
import org.jboss.seam.xml.model.XmlItem;

public class RootNamespaceElementResolver implements NamespaceElementResolver
{

   CompositeNamespaceElementResolver delegate;
   static final String[] namspaces = { "java.lang", "java.util", "javax.annotation", "javax.inject", "javax.enterprise.inject", "javax.enterprise.context", "javax.enterprise.event", "javax.decorator", "javax.interceptor", "javax.persistence", "javax.xml.ws", "javax.jms", "javax.sql" };

   public RootNamespaceElementResolver()
   {
      delegate = new CompositeNamespaceElementResolver(namspaces);
   }

   public XmlItem getItemForNamespace(String item, XmlItem parent, String innerText, Map<String, String> attributes) throws InvalidElementException
   {
      XmlItem ret = getRootItem(item, parent, innerText);
      if (ret != null)
         return ret;
      return delegate.getItemForNamespace(item, parent, innerText, attributes);

   }

   XmlItem getRootItem(String item, XmlItem parent, String innerText)
   {
      if (item.equals("value") || item.equals("v"))
      {
         return new ValueXmlItem(parent, innerText);
      }
      else if (item.equals("key") || item.equals("k"))
      {
         return new KeyXmlItem(parent, innerText);
      }
      else if (item.equals("entry") || item.equals("e"))
      {
         return new EntryXmlItem(parent);
      }
      else if (item.equals("array"))
      {
         return new ArrayXmlItem(parent);
      }
      else if (item.equals("veto"))
      {
         return new VetoXmlItem(parent);
      }
      else if (item.equals("depends"))
      {
         return new DependsXmlItem(parent, innerText);
      }
      return null;
   }

}
