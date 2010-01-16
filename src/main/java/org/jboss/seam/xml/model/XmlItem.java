/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.seam.xml.fieldset.FieldValueObject;

public interface XmlItem
{

   public String getInnerText();

   public void addChild(XmlItem xmlItem);

   public XmlItem getParent();

   public List<XmlItem> getChildren();

   public XmlItemType getType();

   public Field getField();

   public Method getMethod();

   public Class getJavaClass();

   /**
    * attempts to resolve any information that is not availbile at parse time
    * 
    * @param childeren
    * @return
    */
   public boolean resolveChildren();

   public Map<String, String> getAttributes();

   public FieldValueObject getFieldValue();

   public Set<XmlItemType> getAllowedItem();

}