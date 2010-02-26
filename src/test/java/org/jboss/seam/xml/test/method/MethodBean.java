/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.test.method;

public class MethodBean
{

   public int method()
   {
      return 1;
   }

   public int method(MethodValueBean bean)
   {
      return bean.value + 1;
   }

   public long method(MethodValueBean[][] beans)
   {
      return beans.length;
   }

}
