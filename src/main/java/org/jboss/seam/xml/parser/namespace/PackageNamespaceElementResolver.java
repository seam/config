/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.parser.namespace;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jboss.seam.xml.model.AnnotationXmlItem;
import org.jboss.seam.xml.model.ClassXmlItem;
import org.jboss.seam.xml.model.XmlItem;
import org.jboss.seam.xml.model.XmlItemType;

public class PackageNamespaceElementResolver implements NamespaceElementResolver
{
   String pack;
   Map<String, Class> cache = new HashMap<String, Class>();
   Set<String> notFound = new HashSet<String>();

   public PackageNamespaceElementResolver(String pack)
   {
      this.pack = pack + ".";
   }

   public XmlItem getItemForNamespace(String name, XmlItem parent, String innerText, Map<String, String> attributes) throws InvalidElementException
   {
      if (notFound.contains(name))
      {
         return null;
      }

      try
      {
         Class c;
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
            return new AnnotationXmlItem(parent, c, innerText, attributes);
         }
         else
         {
            return new ClassXmlItem(parent, c);
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
            return NamespaceUtils.resolveMethodOrField(name, parent, innerText);
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

}
