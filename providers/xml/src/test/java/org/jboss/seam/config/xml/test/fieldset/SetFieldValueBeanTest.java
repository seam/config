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
package org.jboss.seam.config.xml.test.fieldset;

import java.math.BigDecimal;

import javax.enterprise.util.AnnotationLiteral;

import junit.framework.Assert;

import org.jboss.seam.config.xml.test.AbstractXMLTest;
import org.jboss.seam.config.xml.test.method.QualifierEnum;
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
      Assert.assertTrue(x.readBigDecimalValue().compareTo(BigDecimal.TEN) == 0);
      Assert.assertTrue(x.bvalue == true);
      Assert.assertTrue(x.dvalue == 0);
      Assert.assertTrue(x.enumValue == QualifierEnum.A);
      Assert.assertTrue(x.fvalue == 0);
      Assert.assertTrue(x.getIvalue() == 11);
      Assert.assertTrue(x.lvalue == 23);
      Assert.assertTrue(x.svalue == 4);
      Assert.assertTrue(x.noFieldValue == 7);
      Assert.assertEquals(ELValueProducer.EL_VALUE_STRING, x.elValue);
      Assert.assertEquals(ELValueProducer.EL_VALUE_STRING, x.elInnerTextValue);
   }

   @Test
   public void simpleShorthandFieldSetterTest()
   {
      FieldValueBean x = getReference(FieldValueBean.class, new AnnotationLiteral<FieldsetQualifier>()
      {
      });
      Assert.assertTrue(x.readBigDecimalValue().compareTo(BigDecimal.TEN) == 0);
      Assert.assertTrue(x.bvalue == true);
      Assert.assertTrue(x.dvalue == 0);
      Assert.assertTrue(x.enumValue == QualifierEnum.A);
      Assert.assertTrue(x.fvalue == 0);
      Assert.assertTrue(x.getIvalue() == 11);
      Assert.assertTrue(x.lvalue == 23);
      Assert.assertTrue(x.svalue == 4);
      Assert.assertTrue(x.noFieldValue == 7);

   }

}
