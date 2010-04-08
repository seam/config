package org.jboss.seam.xml.test.types;


public class TypeInjectedClass
{
   SomeInterface createValue;

   public void create(SomeInterface value)
   {
      this.createValue = value;
   }

   public SomeInterface value;

}
