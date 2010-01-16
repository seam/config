/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.test.injection;

import org.jboss.seam.xml.test.method.QualifierEnum;

public @interface OtherQualifier
{
   String value1();

   int value2();

   QualifierEnum value();
}
