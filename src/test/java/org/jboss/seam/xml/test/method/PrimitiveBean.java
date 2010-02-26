package org.jboss.seam.xml.test.method;

public class PrimitiveBean
{
   public int add(int val)
   {
      return val + 1;
   }

   public int[] add(int[] val)
   {
      for (int i = 0; i < val.length; ++i)
      {
         val[i] = val[i] + 1;
      }
      return val;
   }
}
