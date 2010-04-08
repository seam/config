/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.parser.namespace;

/**
 * Exception thrown when both a method and a field have the same name
 */
public class InvalidElementException extends Exception
{
   public InvalidElementException(String message)
   {
      super(message);
   }
}
