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
package org.jboss.seam.xml.test.generic;

import javax.enterprise.util.AnnotationLiteral;

import org.jboss.seam.xml.test.AbstractXMLTest;
import org.junit.Test;

public class GenericBeanTest extends AbstractXMLTest
{

   @Override
   protected String getXmlFileName()
   {
      return "generic-beans.xml";
   }

   @Test
   public void genericBeansTest()
   {
      GenericDependant dep = getReference(GenericDependant.class, new AnnotationLiteral<HighGenericQualifier>()
      {
      });
      // assert dep.getValue() == 101 : " actual " + dep.getValue();
      dep = getReference(GenericDependant.class, new AnnotationLiteral<LowGenericQualifier>()
      {
      });
      assert dep.getValue() == 11 : " actual " + dep.getValue();
   }

   @Test
   public void genericBeansInitMethodTest()
   {
      GenericDependant dep = getReference(GenericDependant.class, new AnnotationLiteral<HighGenericQualifier>()
      {
      });
      assert dep.initCalled;
      dep = getReference(GenericDependant.class, new AnnotationLiteral<LowGenericQualifier>()
      {
      });
      assert dep.initCalled;
      GenericMain main = getReference(GenericMain.class, new AnnotationLiteral<LowGenericQualifier>()
      {
      });
      assert main.init;
   }

}
