/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.jboss.seam.xml.model.FieldXmlItem;
import org.jboss.seam.xml.model.MethodXmlItem;
import org.jboss.seam.xml.model.XmlItem;

public class ReflectionUtils
{
   public static XmlItem resolveMethodOrField(String name, XmlItem parent, String innerText, String document, int lineno)
   {
      Class<?> p = parent.getJavaClass();
      Field f = null;
      boolean methodFound = methodExists(p, name);
      f = getField(p, name);

      if (methodFound && f != null)
      {
         throw new XmlConfigurationException(parent.getJavaClass().getName() + " has both a method and a field named " + name + " and so cannot be configured via XML", document, lineno);
      }
      if (methodFound)
      {
         return new MethodXmlItem(parent, name, document, lineno);
      }
      else if (f != null)
      {
         return new FieldXmlItem(parent, f, innerText, document, lineno);
      }
      return null;
   }

   /**
    * we need access to private fields so we cannot just use getField
    */
   public static Field getField(Class<?> parent, String name)
   {
      Class<?> p = parent;
      while (p != Object.class)
      {
         try
         {
            return p.getDeclaredField(name);
         }
         catch (Exception e1)
         {

         }
         p = p.getSuperclass();
      }
      return null;
   }

   public static boolean methodExists(Class<?> parent, String name)
   {
      Class<?> p = parent;
      while (p != Object.class)
      {
         for (Method m : p.getDeclaredMethods())
         {
            if (m.getName().equals(name))
            {
               return true;
            }
         }
         p = p.getSuperclass();
      }
      return false;
   }

   public static Method getMethod(Class<?> parent, String name, Class<?>... args)
   {
      Class<?> p = parent;
      while (p != Object.class)
      {
         try
         {
            return p.getDeclaredMethod(name, args);
         }
         catch (Exception e1)
         {

         }
         p = p.getSuperclass();
      }
      return null;
   }

   public static Constructor<?> getConstructor(Class<?> parent, Class<?>... args)
   {
      Class<?> p = parent;
      while (p != Object.class)
      {
         try
         {
            return p.getDeclaredConstructor(args);
         }
         catch (Exception e1)
         {

         }
         p = p.getSuperclass();
      }
      return null;
   }
}
