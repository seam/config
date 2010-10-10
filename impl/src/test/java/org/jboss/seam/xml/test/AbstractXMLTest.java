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
package org.jboss.seam.xml.test;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.After;
import org.junit.Before;

public abstract class AbstractXMLTest
{
   protected BeanManager manager;

   Weld weld;

   protected abstract String getXmlFileName();

   @Before
   public void setup()
   {
      String fileName = getClass().getPackage().getName().replace('.', '/') + "/" + getXmlFileName();
      SimpleXmlProvider.fileName = fileName;
      weld = new Weld();
      WeldContainer container = weld.initialize();
      manager = container.getBeanManager();
   }

   @After
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
         for (Annotation a : bindings)
         {
            bs.append(a.toString() + ",");
         }
         bs.append("]");
         throw new RuntimeException("More than one bean found with class: " + clazz + " and bindings " + bs);
      }
      Bean<?> bean = beans.iterator().next();
      return (T) manager.getReference(bean, clazz, manager.createCreationalContext(bean));
   }

}
