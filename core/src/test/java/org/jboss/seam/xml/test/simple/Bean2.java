/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.test.simple;

import java.util.Set;

public class Bean2
{
   public void method1()
   {

   }

   public String[] arrayField;

   public int intField;
   public String stringField;

   public Set<String> setField;

   public Bean3 produceBean3()
   {
      return new Bean3();
   }
}
