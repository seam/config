/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.test.simple;

import java.util.Set;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.seam.xml.test.AbstractXMLTest;
import org.jboss.weld.environment.se.util.WeldManagerUtils;
import org.testng.annotations.Test;

/**
 * Unit test for simple App.
 */
public class SimpleBeanTest extends AbstractXMLTest
{

   @Override
   protected String getXmlFileName()
   {
      return "simple-beans.xml";
   }

   @Test
   public void simpleBeanTest()
   {
      Bean1 x = WeldManagerUtils.getInstanceByType(manager, Bean1.class);
      assert x != null;
      assert x.bean2 != null;

      Bean3 y = WeldManagerUtils.getInstanceByType(manager, Bean3.class);
      assert y != null;
      assert x.value == 1 : "Post construct method not called";
   }

   @Test
   public void testOverride()
   {
      Set<Bean<?>> beans = manager.getBeans(OverriddenBean.class);
      assert beans.size() == 1;
      assert beans.iterator().next().getName().equals("someBean");

   }

   @Test
   public void testExtends()
   {
      AnnotationLiteral<ExtendedQualifier1> e1 = new AnnotationLiteral<ExtendedQualifier1>()
      {
      };
      AnnotationLiteral<ExtendedQualifier2> e2 = new AnnotationLiteral<ExtendedQualifier2>()
      {
      };

      ExtendedBean ext = getReference(ExtendedBean.class, e1, e2);
      assert ext != null;

   }
}
