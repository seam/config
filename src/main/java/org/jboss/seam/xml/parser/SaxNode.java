/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SaxNode
{

   public SaxNode(String name, String uri, Map<String, String> attributes, SaxNode parent, String document, int lineNo)
   {
      this.name = name;
      this.namespaceUri = uri;
      this.attributes = attributes;
      this.parent = parent;
      this.document = document;
      this.lineNo = lineNo;
   }

   String innerText;
   final String namespaceUri;
   final String name;
   final Map<String, String> attributes;
   final String document;
   final int lineNo;
   List<SaxNode> children = new ArrayList<SaxNode>();
   final SaxNode parent;

   public String getInnerText()
   {
      if (innerText != null && innerText.length() == 0)
      {
         return null;
      }
      return innerText;
   }

   public void setInnerText(String innerText)
   {
      this.innerText = innerText.trim();
   }

   public String getNamespaceUri()
   {
      return namespaceUri;
   }

   public String getName()
   {
      return name;
   }

   public Map<String, String> getAttributes()
   {
      return attributes;
   }

   public String getDocument()
   {
      return document;
   }

   public int getLineNo()
   {
      return lineNo;
   }

   public void addChild(SaxNode node)
   {
      this.children.add(node);
   }

   public List<SaxNode> getChildren()
   {
      return Collections.unmodifiableList(children);
   }

   public SaxNode getParent()
   {
      return parent;
   }

}
