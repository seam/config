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
package org.jboss.seam.config.xml.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.jboss.seam.config.xml.model.MethodXmlItem;
import org.jboss.seam.config.xml.model.PropertyXmlItem;
import org.jboss.seam.config.xml.model.XmlItem;
import org.jboss.seam.config.xml.model.XmlItemType;
import org.jboss.seam.config.xml.parser.SaxNode;
import org.jboss.seam.config.xml.parser.namespace.CompositeNamespaceElementResolver;
import org.jboss.seam.config.xml.parser.namespace.NamespaceElementResolver;
import org.jboss.seam.config.xml.parser.namespace.PackageNamespaceElementResolver;
import org.jboss.seam.config.xml.test.simple.Bean1;
import org.junit.Test;

public class NamespaceResolverTest
{

   @Test
   public void testPackageResolver()
   {
      PackageNamespaceElementResolver resolver = new PackageNamespaceElementResolver("org.jboss.seam.config.xml.test.simple");
      testResolver(resolver);
   }

   @Test
   public void testCompositePackageResolver()
   {
      List<String> namespaces = new ArrayList<String>();
      namespaces.add("java.lang");
      namespaces.add("java.util");
      namespaces.add("org.jboss.seam.config.xml.test.simple");
      CompositeNamespaceElementResolver resolver = new CompositeNamespaceElementResolver(namespaces);
      testResolver(resolver);
   }

   public void testResolver(NamespaceElementResolver resolver)
   {

      XmlItem item = resolver.getItemForNamespace(new SaxNode("Bean1", null, null, null, null, 0), null);
      Assert.assertTrue("Namespace resolver returned wrong class type", item.getJavaClass() == Bean1.class);
      Assert.assertTrue("Namespace resolver did not return class", item.getType() == XmlItemType.CLASS);
      XmlItem method = resolver.getItemForNamespace(new SaxNode("method1", null, null, null, null, 0), item);
      Assert.assertTrue("Item returned wrong type", method.getType() == XmlItemType.METHOD);

      method.resolveChildren(null);

      Assert.assertTrue("Could not resolve method", ((MethodXmlItem) method).getMethod() != null);
      Assert.assertTrue("Wrong method was resolved", ((MethodXmlItem) method).getMethod().getParameterTypes().length == 0);

      XmlItem field = resolver.getItemForNamespace(new SaxNode("field1", null, null, null, null, 0), item);
      Assert.assertTrue("Element of wrong type returned", ((PropertyXmlItem) field).getType() == XmlItemType.FIELD);
      Assert.assertTrue("field was not set", ((PropertyXmlItem) field).getField() != null);

   }

}
