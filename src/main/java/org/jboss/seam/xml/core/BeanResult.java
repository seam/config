/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jboss.weld.extensions.util.annotated.NewAnnotatedTypeBuilder;

public class BeanResult<X>
{
   NewAnnotatedTypeBuilder<X> builder;
   List<String> dependencies = new ArrayList<String>();
   Class<X> type;

   public BeanResult(Class<X> type)
   {
      this.type = type;
      builder = new NewAnnotatedTypeBuilder<X>(type);
   }

   public NewAnnotatedTypeBuilder<X> getBuilder()
   {
      return builder;
   }

   public void addDependency(String className)
   {
      dependencies.add(className);
   }

   public List<String> getDependencies()
   {
      return Collections.unmodifiableList(dependencies);
   }

   public Class<X> getType()
   {
      return type;
   }
}
