package org.jboss.seam.xml.util;


public class XmlParseException extends RuntimeException
{
   int lineno;
   String document;

   public XmlParseException(Exception e, String document, int lineno)
   {
      super(e);
      this.lineno = lineno;
      this.document = document;
   }

   @Override
   public String getMessage()
   {
      return "Error parsing XML document " + document + ":" + lineno + " " + super.getMessage();
   }
}
