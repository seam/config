package org.jboss.seam.xml.examples.princess;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@RequestScoped
@Named
public class GameMessage implements Serializable
{

   StringBuilder builder = new StringBuilder();

   public void add(String message)
   {
      if (message != null)
      {
         builder.append(message);
         builder.append('\n');
      }
   }

   public String getMessage()
   {
      return builder.toString();
   }

}
