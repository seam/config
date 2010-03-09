/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.test.interceptor;

import javax.interceptor.InvocationContext;

public class InterceptorBean
{
   public Object myMethod(InvocationContext context) throws Exception
   {
      return context.proceed().toString() + " world";
   }
}
