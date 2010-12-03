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
package org.jboss.seam.config.xml.parser.namespace;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.seam.config.xml.model.XmlItem;
import org.jboss.seam.config.xml.parser.SaxNode;

/**
 * Namespace resolver that searches through a list of packages
 * 
 * @author Stuart Douglas <stuart@baileyroberts.com.au>
 * 
 */
public class CompositeNamespaceElementResolver implements NamespaceElementResolver
{

   private final Set<String> notFound = new HashSet<String>();
   private final List<PackageNamespaceElementResolver> resolvers = new ArrayList<PackageNamespaceElementResolver>();

   public CompositeNamespaceElementResolver(Collection<String> packages)
   {
      for (String s : packages)
      {
         resolvers.add(new PackageNamespaceElementResolver(s));
      }
   }

   public CompositeNamespaceElementResolver(String[] packages)
   {
      for (String s : packages)
      {
         resolvers.add(new PackageNamespaceElementResolver(s));
      }
   }

   public XmlItem getItemForNamespace(SaxNode node, XmlItem parent)
   {
      if (notFound.contains(node.getName()))
      {
         return null;
      }

      for (PackageNamespaceElementResolver p : resolvers)
      {
         XmlItem xi = p.getItemForNamespace(node, parent);
         if (xi != null)
         {
            return xi;
         }
      }
      notFound.add(node.getName());
      return null;
   }

}
