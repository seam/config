/*
 * Distributed under the LGPL License
 * 
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
import org.testng.annotations.Test;

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
