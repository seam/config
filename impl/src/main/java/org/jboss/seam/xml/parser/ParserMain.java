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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.seam.xml.util.XmlParseException;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Parser for xml configration, this class should only be used once and then
 * discarded
 * 
 * @author Stuart Douglas <stuart@baileyroberts.com.au>
 * 
 */
public class ParserMain extends DefaultHandler
{

   SaxNode parentNode = null;

   SaxNode currentNode = null;

   Locator locator;

   String currentText = "";

   String document;

   List<Exception> errors;

   public SaxNode parse(InputSource inputSource, String fileUrl, List<Exception> errors)
   {
      this.errors = errors;
      document = fileUrl;
      try
      {
         XMLReader xr = XMLReaderFactory.createXMLReader();
         xr.setContentHandler(this);
         xr.setErrorHandler(this);
         xr.parse(inputSource);
         return parentNode;
      }
      catch (SAXException e)
      {
         throw new RuntimeException(e);
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }

   }

   @Override
   public void characters(char[] ch, int start, int length) throws SAXException
   {
      currentText = currentText + new String(ch, start, length);
   }

   @Override
   public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
   {
      Map<String, String> atmap = new HashMap<String, String>();
      for (int i = 0; i < attributes.getLength(); ++i)
      {
         atmap.put(attributes.getLocalName(i), attributes.getValue(i));
      }
      int pos = 0;
      if (locator != null)
      {
         pos = locator.getLineNumber();
      }
      SaxNode node = new SaxNode(localName, uri, atmap, currentNode, document, pos);
      if (currentNode == null)
      {
         parentNode = node;
      }
      else
      {
         currentNode.addChild(node);
      }

      currentNode = node;
      currentText = "";
   }

   @Override
   public void endElement(String uri, String localName, String qName) throws SAXException
   {
      currentNode.setInnerText(currentText);
      currentNode = currentNode.getParent();
      currentText = "";
   }

   @Override
   public void setDocumentLocator(Locator locator)
   {
      this.locator = locator;
   }

   @Override
   public void error(SAXParseException e) throws SAXException
   {
      errors.add(new XmlParseException(e, document, e.getLineNumber()));
   }

   @Override
   public void fatalError(SAXParseException e) throws SAXException
   {
      errors.add(new XmlParseException(e, document, e.getLineNumber()));
   }
}
