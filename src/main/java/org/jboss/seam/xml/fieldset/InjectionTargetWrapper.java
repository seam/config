/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.fieldset;

import java.util.List;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;

public class InjectionTargetWrapper<T> implements InjectionTarget<T>
{
   InjectionTarget<T> target;
   List<FieldValueObject> fieldValues;

   public InjectionTargetWrapper(InjectionTarget<T> target, List<FieldValueObject> fieldValues)
   {
      this.fieldValues = fieldValues;
      this.target = target;
   }

   public void inject(T instance, CreationalContext<T> ctx)
   {
      target.inject(instance, ctx);

   }

   public void postConstruct(T instance)
   {
      for (FieldValueObject f : fieldValues)
      {
         f.setValue(instance);
      }
      target.postConstruct(instance);

   }

   public void preDestroy(T instance)
   {
      target.preDestroy(instance);
   }

   public void dispose(T instance)
   {
      target.dispose(instance);
   }

   public Set<InjectionPoint> getInjectionPoints()
   {
      return target.getInjectionPoints();
   }

   public T produce(CreationalContext<T> ctx)
   {
      return target.produce(ctx);
   }

}
