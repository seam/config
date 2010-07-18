package org.jboss.seam.xml.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jboss.seam.xml.util.XmlConfigurationException;
import org.jboss.seam.xml.util.XmlObjectConverter;
import org.jboss.weld.extensions.util.AnnotationInstanceProvider;

class AnnotationUtils
{
   final private static AnnotationInstanceProvider annotationInstanceProvider = new AnnotationInstanceProvider();

   @SuppressWarnings("unchecked")
   static Annotation createAnnotation(AnnotationXmlItem item)
   {
      Map<String, Object> typedVars = new HashMap<String, Object>();
      Class<?> anClass = item.getJavaClass();
      for (Entry<String, String> e : item.getAttributes().entrySet())
      {
         String mname = e.getKey();
         Method m;
         try
         {
            m = anClass.getDeclaredMethod(mname);
         }
         catch (Exception e1)
         {
            throw new XmlConfigurationException("Annotation " + item.getJavaClass().getName() + " does not have a member named " + mname + " ,error in XML", item.getDocument(), item.getLineno());
         }
         Class<?> returnType = m.getReturnType();
         typedVars.put(mname, XmlObjectConverter.convert(returnType, e.getValue()));
      }

      return annotationInstanceProvider.get((Class) item.getJavaClass(), typedVars);
   }
}
