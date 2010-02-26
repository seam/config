/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.parser.namespace;

import org.jboss.seam.xml.model.ArrayXmlItem;
import org.jboss.seam.xml.model.EntryXmlItem;
import org.jboss.seam.xml.model.KeyXmlItem;
import org.jboss.seam.xml.model.ValueXmlItem;
import org.jboss.seam.xml.model.VetoXmlItem;
import org.jboss.seam.xml.model.XmlItem;
import org.jboss.seam.xml.parser.SaxNode;

public class RootNamespaceElementResolver implements NamespaceElementResolver
{

   CompositeNamespaceElementResolver delegate;
   static final String[] namspaces = { "java.lang", "java.util", "javax.annotation", "javax.inject", "javax.enterprise.inject", "javax.enterprise.context", "javax.enterprise.event", "javax.decorator", "javax.interceptor", "javax.persistence", "javax.xml.ws", "javax.jms", "javax.sql" };

   public RootNamespaceElementResolver()
   {
      delegate = new CompositeNamespaceElementResolver(namspaces);
   }

   public XmlItem getItemForNamespace(SaxNode node, XmlItem parent)
   {
      XmlItem ret = getRootItem(node, parent);
      if (ret != null)
         return ret;
      return delegate.getItemForNamespace(node, parent);

   }

   XmlItem getRootItem(SaxNode node, XmlItem parent)
   {
      String item = node.getName();
      if (item.equals("value") || item.equals("v"))
      {
         return new ValueXmlItem(parent, node.getInnerText(), node.getDocument(), node.getLineNo());
      }
      else if (item.equals("key") || item.equals("k"))
      {
         return new KeyXmlItem(parent, node.getInnerText(), node.getDocument(), node.getLineNo());
      }
      else if (item.equals("entry") || item.equals("e"))
      {
         return new EntryXmlItem(parent, node.getDocument(), node.getLineNo());
      }
      else if (item.equals("array"))
      {
         return new ArrayXmlItem(parent, node.getDocument(), node.getLineNo());
      }
      else if (item.equals("veto"))
      {
         return new VetoXmlItem(parent, node.getDocument(), node.getLineNo());
      }
     
      return null;
   }

}
