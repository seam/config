package org.jboss.seam.xml.test.constructor;

public class ConstructedBean
{
   public ConstructedBean(int val)
   {
      this.value = val;
   }

   public ConstructedBean()
   {

   }

   int value;

   public int getValue()
   {
      return value;
   }

}
