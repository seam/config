/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.seam.xml.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * represents an XML element
 * 
 * @author Stuart Douglas <stuart@baileyroberts.com.au>
 * 
 */
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
