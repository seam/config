/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.model;

import java.util.List;
import java.util.Set;

public interface XmlItem
{

   public String getInnerText();

   public void addChild(XmlItem xmlItem);

   public XmlItem getParent();

   public List<XmlItem> getChildren();

   public XmlItemType getType();

   public Class getJavaClass();

   /**
    * attempts to resolve any information that is not available at parse time
    * 
    * @param childeren
    * @return
    */
   public boolean resolveChildren();

   public Set<XmlItemType> getAllowedItem();

   int getLineno();

   String getDocument();

   public <T> List<T> getChildrenOfType(Class<T> type);

}