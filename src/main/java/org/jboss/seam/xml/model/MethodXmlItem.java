/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.seam.xml.util.XmlConfigurationException;

public class MethodXmlItem extends AbstractXmlItem
{

   private String methodName;

   Method method;
   HashSet<XmlItemType> allowed = new HashSet<XmlItemType>();

   public MethodXmlItem(XmlItem parent, String methodName, String document, int lineno)
   {
      super(XmlItemType.METHOD, parent, parent.getJavaClass(), null, null, document, lineno);

      allowed.add(XmlItemType.ANNOTATION);
      allowed.add(XmlItemType.PARAMETERS);

      // methods are lazily resolved once we know the parameter types
      this.methodName = methodName;
      Method found = null;
      for (Method m : javaClass.getMethods())
      {
         if (m.getName().equals(methodName))
         {
            if (found == null)
            {
               found = m;
            }
            else
            {
               // we have to methods with the same name so resolution
               // will have to wait
               return;
            }
         }
      }
      method = found;
   }

   /**
    * attempts to resolve a lazy method declaration. Returns true if it succeeds
    * or is unessesary, false otherwise
    * 
    * @param childeren
    * @return
    */
   public boolean resolveChildren()
   {
      // return true if this is not a method or there was only
      // only method to choose from
      if (method != null)
      {
         return true;
      }

      List<Class<?>> rtList = new ArrayList<Class<?>>();
      List<ParametersXmlItem> parameters = getChildrenOfType(ParametersXmlItem.class);
      if (parameters.size() > 1)
      {
         throw new XmlConfigurationException("A method may only have a single <parameters> element", document, lineno);
      }
      else if (!parameters.isEmpty())
      {
         for (ParameterXmlItem c : parameters.get(0).getChildrenOfType(ParameterXmlItem.class))
         {
            Class<?> cl = c.getJavaClass();
            rtList.add(cl);
         }
      }
      Class<?>[] alAr = new Class[rtList.size()];
      for (int i = 0; i < rtList.size(); ++i)
      {
         alAr[i] = rtList.get(i);
      }

      try
      {
         method = javaClass.getMethod(methodName, alAr);
         return true;
      }
      catch (SecurityException e)
      {
         throw new XmlConfigurationException("Security Exception resolving method " + methodName + " on class " + javaClass.getName(), getDocument(), getLineno());
      }
      catch (NoSuchMethodException e)
      {
         throw new XmlConfigurationException("NoSuchMethodException resolving method " + methodName + " on class " + javaClass.getName(), getDocument(), getLineno());
      }
   }

   public Method getMethod()
   {
      return method;
   }

   public Set<XmlItemType> getAllowedItem()
   {
      return allowed;
   }
}
