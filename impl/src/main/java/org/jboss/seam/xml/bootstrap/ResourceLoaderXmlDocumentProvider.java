/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.bootstrap;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jboss.weld.extensions.resourceLoader.ResourceLoader;
import org.jboss.weld.extensions.util.service.ServiceLoader;
import org.xml.sax.InputSource;

/**
 * Document Provider that loads XML documents from the classpath
 * 
 * @author Stuart Douglas <stuart@baileyroberts.com.au>
 * 
 */
public class ResourceLoaderXmlDocumentProvider implements XmlDocumentProvider
{

   private final List<ResourceLoader> resourceLoaders;

   static final String[] DEFAULT_RESOURCES = { "seam-beans.xml", "META-INF/seam-beans.xml", "META-INF/beans.xml" };

   final String[] resources;

   InputStream stream;

   public ResourceLoaderXmlDocumentProvider()
   {
      this(DEFAULT_RESOURCES);
   }

   public ResourceLoaderXmlDocumentProvider(String[] resources)
   {
      this.resources = resources;
      resourceLoaders = new ArrayList<ResourceLoader>();
      for (ResourceLoader resourceLoader : ServiceLoader.load(ResourceLoader.class))
      {
         resourceLoaders.add(resourceLoader);
      }
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
         Set<URL> e = getResources(i);
         docs.addAll(e);
         iterator = docs.listIterator();
      }
   }

   protected Set<URL> getResources(String resource)
   {
      Set<URL> ret = new HashSet<URL>();
      for (ResourceLoader r : resourceLoaders)
      {
         ret.addAll(r.getResources(resource));
      }
      return ret;
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
