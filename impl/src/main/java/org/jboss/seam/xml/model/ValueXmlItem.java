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

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.enterprise.inject.spi.BeanManager;

import org.jboss.seam.xml.core.BeanResult;
import org.jboss.seam.xml.fieldset.ConstantFieldValue;
import org.jboss.seam.xml.fieldset.FieldValue;
import org.jboss.seam.xml.fieldset.InlineBeanFieldValue;
import org.jboss.seam.xml.fieldset.InlineBeanIdCreator;
import org.jboss.seam.xml.fieldset.InlineBeanQualifier;
import org.jboss.seam.xml.util.TypeOccuranceInformation;
import org.jboss.seam.xml.util.XmlConfigurationException;

public class ValueXmlItem extends AbstractXmlItem
{
   private int syntheticQualifierId;
   private BeanResult<?> inlineBean;
   private BeanManager manager;

   public ValueXmlItem(XmlItem parent, String innerText, String document, int lineno)
   {
      super(XmlItemType.VALUE, parent, null, innerText, null, document, lineno);
   }

   public Set<TypeOccuranceInformation> getAllowedItem()
   {
      return Collections.singleton(TypeOccuranceInformation.of(XmlItemType.CLASS, null, 1));
   }

   public BeanResult<?> getBeanResult(BeanManager manager)
   {
      this.manager = manager;
      List<ClassXmlItem> inlineBeans = getChildrenOfType(ClassXmlItem.class);
      if (!inlineBeans.isEmpty())
      {
         ClassXmlItem inline = inlineBeans.get(0);
         for (AnnotationXmlItem i : inline.getChildrenOfType(AnnotationXmlItem.class))
         {
            if (manager.isQualifier((Class) i.getJavaClass()))
            {
               throw new XmlConfigurationException("Cannot define qualifiers on inline beans", i.getDocument(), i.getLineno());
            }
         }
         syntheticQualifierId = InlineBeanIdCreator.getId();
         AnnotationXmlItem syntheticQualifier = new AnnotationXmlItem(this, InlineBeanQualifier.class, "" + syntheticQualifierId, Collections.EMPTY_MAP, getDocument(), getLineno());
         inline.addChild(syntheticQualifier);
         inlineBean = inline.createBeanResult(manager);
         return inlineBean;
      }
      inlineBean = null;
      return null;
   }

   public int getSyntheticQualifierId()
   {
      return syntheticQualifierId;
   }

   public FieldValue getValue()
   {
      if (inlineBean == null)
      {
         return new ConstantFieldValue(innerText);
      }
      else
      {
         return new InlineBeanFieldValue(syntheticQualifierId, manager);
      }
   }
}
