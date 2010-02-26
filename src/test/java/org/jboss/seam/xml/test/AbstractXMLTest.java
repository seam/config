/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.test;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public abstract class AbstractXMLTest
{

   public static String[] ARGS_EMPTY = new String[] {};

   protected BeanManager manager;
   
   Weld weld;

   protected abstract String getXmlFileName();

   @BeforeClass
   public void setup()
   {
      String fileName = getClass().getPackage().getName().replace('.', '/') + "/" + getXmlFileName();
      TestXmlProvider.fileName = fileName;
      weld = new Weld();
      WeldContainer container = weld.initialize();
      manager = container.getBeanManager();
   }

   @AfterClass
   public void teardown()
   {
     weld.shutdown();
   }

   public <T> T getReference(Class<T> clazz, Annotation... bindings)
   {
      Set<Bean<?>> beans = manager.getBeans(clazz, bindings);
      if (beans.isEmpty())
      {
         throw new RuntimeException("No bean found with class: " + clazz + " and bindings " + bindings.toString());
      }
      else if (beans.size() != 1)
      {
         StringBuilder bs = new StringBuilder("[");
         for(Annotation a : bindings)
         {
            bs.append(a.toString() + ",");
         }
         bs.append("]");
         throw new RuntimeException("More than one bean found with class: " + clazz + " and bindings " + bs);
      }
      Bean bean = beans.iterator().next();
      return (T) bean.create(manager.createCreationalContext(bean));
   }

  
}
