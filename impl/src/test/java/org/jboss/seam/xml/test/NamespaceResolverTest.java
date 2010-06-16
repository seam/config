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

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.xml.model.FieldXmlItem;
import org.jboss.seam.xml.model.MethodXmlItem;
import org.jboss.seam.xml.model.XmlItem;
import org.jboss.seam.xml.model.XmlItemType;
import org.jboss.seam.xml.parser.SaxNode;
import org.jboss.seam.xml.parser.namespace.CompositeNamespaceElementResolver;
import org.jboss.seam.xml.parser.namespace.NamespaceElementResolver;
import org.jboss.seam.xml.parser.namespace.PackageNamespaceElementResolver;
import org.jboss.seam.xml.test.simple.Bean1;
import org.junit.Test;

public class NamespaceResolverTest
{

   @Test
   public void testPackageResolver()
   {
      PackageNamespaceElementResolver resolver = new PackageNamespaceElementResolver("org.jboss.seam.xml.test.simple");
      testResolver(resolver);
   }

   @Test
   public void testCompositePackageResolver()
   {
      List<String> namespaces = new ArrayList<String>();
      namespaces.add("java.lang");
      namespaces.add("java.util");
      namespaces.add("org.jboss.seam.xml.test.simple");
      CompositeNamespaceElementResolver resolver = new CompositeNamespaceElementResolver(namespaces);
      testResolver(resolver);
   }

   public void testResolver(NamespaceElementResolver resolver)
   {

      XmlItem item = resolver.getItemForNamespace(new SaxNode("Bean1", null, null, null, null, 0), null);
      assert item.getJavaClass() == Bean1.class : "Namespace resolver returned wrong class type";
      assert item.getType() == XmlItemType.CLASS : "Namespace resolver did not return class";
      XmlItem method = resolver.getItemForNamespace(new SaxNode("method1", null, null, null, null, 0), item);
      assert method.getType() == XmlItemType.METHOD : "Item returned wrong type";

      method.resolveChildren();

      assert ((MethodXmlItem) method).getMethod() != null : "Could not resolve method";
      assert ((MethodXmlItem) method).getMethod().getParameterTypes().length == 0 : "Wrong method was resolved";

      XmlItem field = resolver.getItemForNamespace(new SaxNode("field1", null, null, null, null, 0), item);
      assert ((FieldXmlItem) field).getType() == XmlItemType.FIELD : "Element of wrong type returned";
      assert ((FieldXmlItem) field).getField() != null : "field was not set";

   }

}
