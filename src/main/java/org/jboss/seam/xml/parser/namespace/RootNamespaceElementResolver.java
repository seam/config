/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.parser.namespace;

import org.jboss.seam.xml.model.ArrayXmlItem;
import org.jboss.seam.xml.model.ClassXmlItem;
import org.jboss.seam.xml.model.EntryXmlItem;
import org.jboss.seam.xml.model.ExtendsXmlItem;
import org.jboss.seam.xml.model.KeyXmlItem;
import org.jboss.seam.xml.model.OverrideXmlItem;
import org.jboss.seam.xml.model.ParameterXmlItem;
import org.jboss.seam.xml.model.ValueXmlItem;
import org.jboss.seam.xml.model.VetoXmlItem;
import org.jboss.seam.xml.model.XmlItem;
import org.jboss.seam.xml.model.XmlItemType;
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
         return new ArrayXmlItem(parent, node.getAttributes(), node.getDocument(), node.getLineNo());
      }
      else if (item.equals("veto"))
      {
         return new VetoXmlItem(parent, node.getDocument(), node.getLineNo());
      }
      else if (item.equals("override"))
      {
         return new OverrideXmlItem(parent, node.getDocument(), node.getLineNo());
      }
      else if (item.equals("extends"))
      {
         return new ExtendsXmlItem(parent, node.getDocument(), node.getLineNo());
      }

      // now deal with primitive types

      Class primType = null;
      if (item.equals("int"))
      {
         primType = int.class;
      }
      else if (item.equals("short"))
      {
         primType = short.class;
      }
      else if (item.equals("long"))
      {
         primType = long.class;
      }
      else if (item.equals("byte"))
      {
         primType = byte.class;
      }
      else if (item.equals("char"))
      {
         primType = char.class;
      }
      else if (item.equals("double"))
      {
         primType = double.class;
      }
      else if (item.equals("float"))
      {
         primType = float.class;
      }
      else if (item.equals("boolean"))
      {
         primType = boolean.class;
      }
      if (primType != null)
      {
         if (parent != null && (parent.getType() == XmlItemType.METHOD || parent.getType() == XmlItemType.CLASS))
         {
            return new ParameterXmlItem(parent, primType, node.getDocument(), node.getLineNo());
         }
         else
         {
            return new ClassXmlItem(parent, primType, node.getDocument(), node.getLineNo());
         }
      }

      return null;
   }

}
