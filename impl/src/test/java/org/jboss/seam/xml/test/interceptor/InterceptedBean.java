/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.test.interceptor;

@InterceptorBinding
public class InterceptedBean
{

   public String method()
   {
      return "hello";
   }

}
