package org.jboss.seam.xml.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class TypeReader
{
   public static Class<?> readClassFromType(Type type)
   {
      if (type instanceof Class)
      {
         return (Class) type;
      }
      else if (type instanceof ParameterizedType)
      {
         ParameterizedType p = (ParameterizedType) type;
         return TypeReader.readClassFromType(p.getRawType());
      }
      else
      {
         throw new RuntimeException("Cannot convert " + type + " into a java.lang.Class");
      }
   }
}
