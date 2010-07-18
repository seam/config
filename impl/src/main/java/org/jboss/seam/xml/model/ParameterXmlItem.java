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
package org.jboss.seam.xml.model;

import java.util.HashSet;
import java.util.Set;

import org.jboss.seam.xml.util.TypeOccuranceInformation;

/**
 * represents a parameter of a constructor or method
 * 
 * @author Stuart Douglas <stuart@baileyroberts.com.au>
 * 
 */
public class ParameterXmlItem extends AbstractXmlItem
{
   HashSet<TypeOccuranceInformation> allowed = new HashSet<TypeOccuranceInformation>();

   public ParameterXmlItem(XmlItem parent, Class<?> c, String document, int lineno)
   {
      super(XmlItemType.PARAMETER, parent, c, null, null, document, lineno);
      allowed.add(TypeOccuranceInformation.of(XmlItemType.ANNOTATION, null, null));
      allowed.add(TypeOccuranceInformation.of(XmlItemType.CLASS, null, 1));
      allowed.add(TypeOccuranceInformation.of(XmlItemType.ARRAY, null, 1));
   }

   public Set<TypeOccuranceInformation> getAllowedItem()
   {
      return allowed;
   }

}
