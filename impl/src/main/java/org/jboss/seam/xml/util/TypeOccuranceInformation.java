package org.jboss.seam.xml.util;

import java.util.Set;

import org.jboss.seam.xml.model.XmlItemType;

public class TypeOccuranceInformation
{
   private final XmlItemType type;
   private final Integer minOccurances;
   private final Integer maxOccurances;

   public TypeOccuranceInformation(XmlItemType type, Integer minOccurances, Integer maxOccurances)
   {
      this.type = type;
      this.minOccurances = minOccurances;
      this.maxOccurances = maxOccurances;
   }

   public XmlItemType getType()
   {
      return type;
   }

   public Integer getMinOccurances()
   {
      return minOccurances;
   }

   public Integer getMaxOccurances()
   {
      return maxOccurances;
   }

   public static TypeOccuranceInformation of(XmlItemType type, Integer min, Integer max)
   {
      return new TypeOccuranceInformation(type, min, min);
   }

   public static boolean isTypeInSet(Set<TypeOccuranceInformation> set, XmlItemType type)
   {
      for (TypeOccuranceInformation i : set)
      {
         if (i.getType() == type)
         {
            return true;
         }
      }
      return false;
   }
}
