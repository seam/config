package org.jboss.seam.xml.test.iface;

public class InterfaceBean implements Interface1
{

   String message;

   public void setMessage(String value)
   {
      this.message = value;
   }

   public String value()
   {
      return "hi world";
   }

}
