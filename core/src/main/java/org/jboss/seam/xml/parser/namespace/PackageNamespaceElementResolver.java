/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.parser.namespace;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jboss.seam.xml.model.AnnotationXmlItem;
import org.jboss.seam.xml.model.ClassXmlItem;
import org.jboss.seam.xml.model.FieldXmlItem;
import org.jboss.seam.xml.model.MethodXmlItem;
import org.jboss.seam.xml.model.ParameterXmlItem;
import org.jboss.seam.xml.model.PropertyXmlItem;
import org.jboss.seam.xml.model.XmlItem;
import org.jboss.seam.xml.model.XmlItemType;
import org.jboss.seam.xml.parser.SaxNode;
import org.jboss.seam.xml.util.XmlConfigurationException;
import org.jboss.weld.extensions.util.ReflectionUtils;

public class PackageNamespaceElementResolver implements NamespaceElementResolver
{
   String pack;
   Map<String, Class<?>> cache = new HashMap<String, Class<?>>();
   Set<String> notFound = new HashSet<String>();

   public PackageNamespaceElementResolver(String pack)
   {
      this.pack = pack + ".";
   }

   public XmlItem getItemForNamespace(SaxNode node, XmlItem parent)
   {
      String name = node.getName();
      if (notFound.contains(name))
      {
         return null;
      }

      try
      {
         Class<?> c;
         if (cache.containsKey(name))
         {
            c = cache.get(name);
         }
         else
         {
            c = getClass().getClassLoader().loadClass(pack + name);
            cache.put(name, c);
         }
         if (c.isAnnotation())
         {
            return new AnnotationXmlItem(parent, c, node.getInnerText(), node.getAttributes(), node.getDocument(), node.getLineNo());
         }
         else
         {
            // if it is a method or constructor parameter
            if (parent != null && parent.getType() == XmlItemType.PARAMETERS)
            {
               return new ParameterXmlItem(parent, c, node.getDocument(), node.getLineNo());
            }
            else
            {
               return new ClassXmlItem(parent, c, node.getDocument(), node.getLineNo());
            }
         }

      }
      catch (ClassNotFoundException e)
      {

      }
      catch (NoClassDefFoundError e) // this can get thrown when there is a
      // case insensitive file system
      {

      }
      if (parent != null)
      {
         // if the item can be a method of a FIELD
         if (parent.getAllowedItem().contains(XmlItemType.METHOD) || parent.getAllowedItem().contains(XmlItemType.FIELD))
         {
            return resolveMethodOrField(name, parent, node.getInnerText(), node.getDocument(), node.getLineNo());
         }
         else
         {
            notFound.add(name);
         }
      }
      else
      {
         notFound.add(name);
      }
      return null;
   }

   public static XmlItem resolveMethodOrField(String name, XmlItem parent, String innerText, String document, int lineno)
   {
      Class<?> p = parent.getJavaClass();
      Field f = null;
      boolean methodFound = ReflectionUtils.methodExists(p, name);
      f = ReflectionUtils.getField(p, name);

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

      String methodName = "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
      if (ReflectionUtils.methodExists(p, methodName))
      {
         Set<Method> methods = ReflectionUtils.getMethods(p);
         for (Method m : methods)
         {
            if (m.getName().equals(methodName) && m.getParameterTypes().length == 1)
            {
               return new PropertyXmlItem(parent, name, m, innerText, document, lineno);
            }
         }
      }
      return null;
   }
}
