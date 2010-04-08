/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.test.fieldset;

import org.jboss.seam.xml.test.AbstractXMLTest;
import org.jboss.weld.environment.se.util.WeldManagerUtils;
import org.testng.annotations.Test;

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
      CollectionFieldValue x = WeldManagerUtils.getInstanceByType(manager, CollectionFieldValue.class);
      assert x.iset.size() == 2;
      assert x.clist.size() == 2;
      assert x.sset.size() == 2;
      boolean first = true;
      for (String i : x.sset)
      {
         if (first)
         {
            assert i.equals("1");
            first = false;
         }
         else
         {
            assert i.equals("2");
         }

      }
      first = true;
      for (Integer i : x.iset)
      {
         if (first)
         {
            assert i.equals(new Integer(1));
            first = false;
         }
         else
         {
            assert i.equals(new Integer(2));
         }

      }
      assert x.clist.get(0) == Integer.class;
      assert x.clist.get(1) == Long.class;

   }
}
