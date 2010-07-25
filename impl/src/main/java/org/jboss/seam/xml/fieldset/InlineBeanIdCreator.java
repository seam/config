package org.jboss.seam.xml.fieldset;

public class InlineBeanIdCreator
{
   static int count = 0;

   public static int getId()
   {
      return count++;
   }
}
