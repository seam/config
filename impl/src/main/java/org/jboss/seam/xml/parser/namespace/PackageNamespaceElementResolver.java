/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.seam.xml.parser.namespace;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jboss.seam.xml.model.AnnotationXmlItem;
import org.jboss.seam.xml.model.ClassXmlItem;
import org.jboss.seam.xml.model.MethodXmlItem;
import org.jboss.seam.xml.model.ParameterXmlItem;
import org.jboss.seam.xml.model.PropertyXmlItem;
import org.jboss.seam.xml.model.XmlItem;
import org.jboss.seam.xml.model.XmlItemType;
import org.jboss.seam.xml.parser.SaxNode;
import org.jboss.seam.xml.util.TypeOccuranceInformation;
import org.jboss.seam.xml.util.XmlConfigurationException;
import org.jboss.weld.extensions.properties.Property;
import org.jboss.weld.extensions.properties.query.NamedPropertyCriteria;
import org.jboss.weld.extensions.properties.query.PropertyQueries;
import org.jboss.weld.extensions.properties.query.PropertyQuery;
import org.jboss.weld.extensions.reflection.Reflections;

public class PackageNamespaceElementResolver implements NamespaceElementResolver
{
   private final String pack;
   private final Map<String, Class<?>> cache = new HashMap<String, Class<?>>();
   private final Set<String> notFound = new HashSet<String>();

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
               return new ClassXmlItem(parent, c, node.getAttributes(), node.getDocument(), node.getLineNo());
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
         if (TypeOccuranceInformation.isTypeInSet(parent.getAllowedItem(), XmlItemType.METHOD) || TypeOccuranceInformation.isTypeInSet(parent.getAllowedItem(), XmlItemType.FIELD))
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
      boolean methodFound = Reflections.methodExists(p, name);
      PropertyQuery<Object> query = PropertyQueries.createQuery(parent.getJavaClass());
      query.addCriteria(new NamedPropertyCriteria(name));
      Property<Object> property = query.getFirstResult();

      if (methodFound && property != null)
      {
         throw new XmlConfigurationException(parent.getJavaClass().getName() + " has both a method and a property named " + name + " and so cannot be configured via XML", document, lineno);
      }
      if (methodFound)
      {
         return new MethodXmlItem(parent, name, document, lineno);
      }
      else if (property != null)
      {
         // ensure the property is accessible
         Member member = property.getMember();
         if (member instanceof Field)
         {
            Field field = (Field) member;
            field.setAccessible(true);
         }
         else if (member instanceof Method)
         {
            Method method = (Method) member;
            method.setAccessible(true);
         }

         return new PropertyXmlItem(parent, property, innerText, null, document, lineno);
      }
      return null;
   }
}
