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
package org.jboss.seam.xml.test.fieldset;

import java.math.BigDecimal;

import javax.enterprise.util.AnnotationLiteral;

import org.jboss.seam.xml.test.AbstractXMLTest;
import org.jboss.seam.xml.test.method.QualifierEnum;
import org.junit.Test;

public class SetFieldValueBeanTest extends AbstractXMLTest
{

   @Override
   protected String getXmlFileName()
   {
      return "set-field-value-beans.xml";
   }

   @Test
   public void simpleFieldSetterTest()
   {
      FieldValueBean x = getReference(FieldValueBean.class);
      assert x.bigDecimalValue.compareTo(BigDecimal.TEN) == 0;
      assert x.bvalue == true;
      assert x.dvalue == 0;
      assert x.enumValue == QualifierEnum.A;
      assert x.fvalue == 0;
      assert x.getIvalue() == 11;
      assert x.lvalue == 23;
      assert x.svalue == 4;
      assert x.noFieldValue == 7;

   }

   @Test
   public void simpleShorthandFieldSetterTest()
   {
      FieldValueBean x = getReference(FieldValueBean.class, new AnnotationLiteral<FieldsetQualifier>()
      {
      });
      assert x.bigDecimalValue.compareTo(BigDecimal.TEN) == 0;
      assert x.bvalue == true;
      assert x.dvalue == 0;
      assert x.enumValue == QualifierEnum.A;
      assert x.fvalue == 0;
      assert x.getIvalue() == 11;
      assert x.lvalue == 23;
      assert x.svalue == 4;
      assert x.noFieldValue == 7;

   }

}
