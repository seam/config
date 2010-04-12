/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.test.method;

import javax.enterprise.inject.Instance;

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
   
   public int method(Instance<MethodValueBean> bean)
   {
	   return bean.get().getValue() + 1;
   }

}
