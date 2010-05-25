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
