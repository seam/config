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

public class SetCollectionFieldValueBeanTest extends AbstractXMLTest
{

   @Override
   protected String getXmlFileName()
   {
      return "colection-set-field-value-beans.xml";
   }

   @Test
   public void collectionSetFieldValue()
   {
      CollectionFieldValue x = getReference(CollectionFieldValue.class);
      Assert.assertTrue(x.iset.size() == 2);
      Assert.assertTrue(x.clist.size() == 2);
      Assert.assertTrue(x.sset.size() == 2);
      boolean first = true;
      for (String i : x.sset)
      {
         if (first)
         {
            Assert.assertTrue(i.equals("1"));
            first = false;
         }
         else
         {
            Assert.assertTrue(i.equals("2"));
         }

      }
      first = true;
      for (Integer i : x.iset)
      {
         if (first)
         {
            Assert.assertTrue(i.equals(new Integer(1)));
            first = false;
         }
         else
         {
            Assert.assertTrue(i.equals(new Integer(2)));
         }

      }
      Assert.assertTrue(x.clist.get(0) == Integer.class);
      Assert.assertTrue(x.clist.get(1) == Long.class);

   }
}
