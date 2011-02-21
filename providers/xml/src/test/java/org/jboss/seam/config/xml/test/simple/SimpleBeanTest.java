/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.seam.config.xml.test.simple;

import java.lang.reflect.Method;
import java.util.Set;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;

import junit.framework.Assert;

import org.jboss.seam.config.xml.test.AbstractXMLTest;
import org.junit.Test;

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
      Bean1 x = getReference(Bean1.class);
      Assert.assertTrue(x != null);
      Assert.assertTrue(x.bean2 != null);

      Bean2 bean2 = getReference(Bean2.class);
      Assert.assertEquals("test value", bean2.produceBean3);

      Bean3 y = getReference(Bean3.class);
      Assert.assertTrue(y != null);
      Assert.assertTrue("Post construct method not called", x.value == 1);
   }

   @Test
   public void testOverride()
   {
      Set<Bean<?>> beans = manager.getBeans(OverriddenBean.class);
      Assert.assertTrue(beans.size() == 1);
      Assert.assertTrue(beans.iterator().next().getName().equals("someBean"));

   }

   @Test
   public void testExtends() throws SecurityException, NoSuchMethodException
   {
      AnnotationLiteral<ExtendedQualifier1> e1 = new AnnotationLiteral<ExtendedQualifier1>()
      {
      };
      AnnotationLiteral<ExtendedQualifier2> e2 = new AnnotationLiteral<ExtendedQualifier2>()
      {
      };

      ExtendedBean ext = getReference(ExtendedBean.class, e1, e2);
      Assert.assertTrue(ext != null);
      Method method = ext.getClass().getDeclaredMethod("getData");
      method.getGenericReturnType();
   }
}
