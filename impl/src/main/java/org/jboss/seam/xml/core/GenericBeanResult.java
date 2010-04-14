package org.jboss.seam.xml.core;

import java.util.HashSet;
import java.util.Set;

public class GenericBeanResult
{
   final Class genericBean;

   final Set<BeanResult<?>> secondaryBeans;

   public GenericBeanResult(Class<?> genericBean, Set<BeanResult<?>> secondaryBeans)
   {
      this.genericBean = genericBean;
      this.secondaryBeans = new HashSet<BeanResult<?>>(secondaryBeans);
   }

   public Class getGenericBean()
   {
      return genericBean;
   }

   public Set<BeanResult<?>> getSecondaryBeans()
   {
      return secondaryBeans;
   }

}
