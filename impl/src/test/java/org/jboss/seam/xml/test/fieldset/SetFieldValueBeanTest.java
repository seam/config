/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.test.fieldset;

import java.math.BigDecimal;

import javax.enterprise.util.AnnotationLiteral;

import org.jboss.seam.xml.test.AbstractXMLTest;
import org.jboss.seam.xml.test.method.QualifierEnum;
import org.testng.annotations.Test;

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
