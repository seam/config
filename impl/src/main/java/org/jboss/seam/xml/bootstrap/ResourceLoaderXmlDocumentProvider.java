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
package org.jboss.seam.xml.bootstrap;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jboss.weld.extensions.resourceLoader.ResourceLoaderManager;
import org.xml.sax.InputSource;

/**
 * Document Provider that loads XML documents from the classpath
 * 
 * @author Stuart Douglas <stuart@baileyroberts.com.au>
 * 
 */
public class ResourceLoaderXmlDocumentProvider implements XmlDocumentProvider
{

   private final ResourceLoaderManager manager = new ResourceLoaderManager();

   static final String[] DEFAULT_RESOURCES = { "META-INF/seam-beans.xml", "META-INF/beans.xml", "WEB-INF/beans.xml", "WEB-INF/seam-beans.xml" };

   final String[] resources;

   InputStream stream;

   public ResourceLoaderXmlDocumentProvider()
   {
      this(DEFAULT_RESOURCES);
   }

   public ResourceLoaderXmlDocumentProvider(String[] resources)
   {
      this.resources = resources;
   }

   List<URL> docs;

   ListIterator<URL> iterator;

   DocumentBuilderFactory factory;
   DocumentBuilder builder;

   public void open()
   {
      factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      factory.setIgnoringComments(true);
      factory.setIgnoringElementContentWhitespace(true);
      try
      {
         builder = factory.newDocumentBuilder();
      }
      catch (ParserConfigurationException e1)
      {
         throw new RuntimeException(e1);
      }
      docs = new ArrayList<URL>();

      for (String i : resources)
      {
         Collection<URL> e = manager.getResources(i);
         docs.addAll(e);
      }
      iterator = docs.listIterator();
   }

   public void close()
   {
      if (stream != null)
      {
         try
         {
            stream.close();
         }
         catch (Exception e)
         {
            e.printStackTrace();
         }
      }
   }

   public XmlDocument getNextDocument()
   {
      if (stream != null)
      {
         try
         {
            stream.close();
            stream = null;
         }
         catch (Exception e)
         {
            e.printStackTrace();
         }
      }

      try
      {
         while (iterator.hasNext())
         {
            final URL url = iterator.next();
            // ignore empty files
            InputStream test = null;
            try
            {
               test = url.openStream();
               if (test.available() == 0)
               {
                  continue;
               }
            }
            finally
            {
               if (test != null)
               {
                  test.close();
               }
            }

            return new XmlDocument()
            {

               public InputSource getInputSource()
               {
                  try
                  {
                     stream = url.openStream();
                     return new InputSource(stream);
                  }
                  catch (IOException e)
                  {
                     throw new RuntimeException(e);
                  }
               }

               public String getFileUrl()
               {
                  return url.toString();
               }
            };
         }
         return null;
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }

}
