/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.test;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.weld.environment.se.StartMain;
import org.jboss.weld.environment.se.events.Shutdown;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public abstract class AbstractXMLTest
{

   public static String[] ARGS_EMPTY = new String[] {};

   protected BeanManager manager;

   protected abstract String getXmlFileName();

   @BeforeClass
   public void setup()
   {
      String fileName = getClass().getPackage().getName().replace('.', '/') + "/" + getXmlFileName();
      TestXmlProvider.fileName = fileName;
      manager = new StartMain(ARGS_EMPTY).go();
   }

   @AfterClass
   public void teardown()
   {
      manager.fireEvent(manager, new ShutdownAnnotation());
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
         throw new RuntimeException("More than one bean found with class: " + clazz + " and bindings " + bindings.toString());
      }
      Bean bean = beans.iterator().next();
      return (T) bean.create(manager.createCreationalContext(bean));
   }

   protected static class ShutdownAnnotation extends AnnotationLiteral<org.jboss.weld.environment.se.events.Shutdown> implements Shutdown
   {
   }

}
