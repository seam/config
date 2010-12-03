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
package org.jboss.seam.config.xml.test.fieldset;

import junit.framework.Assert;

import org.jboss.seam.config.xml.test.AbstractXMLTest;
import org.junit.Test;

public class SetArrayFieldValueBeanTest extends AbstractXMLTest
{

   @Override
   protected String getXmlFileName()
   {
      return "array-set-field-value-beans.xml";
   }

   @Test
   public void arrayFieldSetterTest()
   {
      ArrayFieldValue x = getReference(ArrayFieldValue.class);
      Assert.assertTrue(x.carray.length == 2);
      Assert.assertTrue(x.iarray.length == 2);
      Assert.assertTrue(x.sarray.length == 2);
      Assert.assertTrue(x.sarray[0].equals("hello"));
      Assert.assertTrue(x.sarray[1].equals("world"));
      Assert.assertTrue(x.iarray[0] == 1);
      Assert.assertTrue(x.iarray[1] == 2);
      Assert.assertTrue(x.carray[0] == Integer.class);
      Assert.assertTrue(x.carray[1] == Long.class);

   }

}
