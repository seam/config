package org.jboss.seam.xml.util;

public class XmlConfigurationException extends Exception
{
   int lineno;
   String document;

   public XmlConfigurationException(String message, String document, int lineno)
   {
      super(message);
      this.document = document;
      this.lineno = lineno;
   }

   @Override
   public String getMessage()
   {
      return super.getMessage() + " at " + document + ":" + lineno;
   }
}
