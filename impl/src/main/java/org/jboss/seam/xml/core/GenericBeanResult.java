package org.jboss.seam.xml.core;

import java.util.HashSet;
import java.util.Set;

public class GenericBeanResult
{
   final Class genericBean;

   final Set<Class> secondaryBeans;

   public GenericBeanResult(Class<?> genericBean, Set<Class> secondaryBeans)
   {
      this.genericBean = genericBean;
      this.secondaryBeans = new HashSet<Class>(secondaryBeans);
   }

   public Class getGenericBean()
   {
      return genericBean;
   }

   public Set<Class> getSecondaryBeans()
   {
      return secondaryBeans;
   }

}
