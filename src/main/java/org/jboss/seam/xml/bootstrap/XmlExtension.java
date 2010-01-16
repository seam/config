/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.bootstrap;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessInjectionTarget;

import org.jboss.seam.xml.core.BeanResult;
import org.jboss.seam.xml.core.XmlId;
import org.jboss.seam.xml.core.XmlResult;
import org.jboss.seam.xml.fieldset.FieldValueObject;
import org.jboss.seam.xml.fieldset.InjectionTargetWrapper;
import org.jboss.seam.xml.model.ModelBuilder;
import org.jboss.seam.xml.parser.ParserMain;
import org.jboss.seam.xml.parser.SaxNode;
import org.jboss.seam.xml.util.FileDataReader;
import org.jboss.weld.extensions.util.AnnotationInstanceProvider;

public class XmlExtension implements Extension
{

   AnnotationInstanceProvider ac = new AnnotationInstanceProvider();

   static final String PROVIDERS_FILE = "META-INF/services/org.jboss.seam.xml.XmlExtension";

   List<XmlResult> results = new ArrayList<XmlResult>();

   Set<Class> veto = new HashSet<Class>();

   Map<Class, AnnotatedType> types = new HashMap<Class, AnnotatedType>();

   int count = 0;

   /**
    * map of syntetic bean id to a list of field value objects
    */
   Map<Integer, List<FieldValueObject>> fieldValues = new HashMap<Integer, List<FieldValueObject>>();

   /**
    * This is the entry point for the extension
    */
   public void beforeBeanDiscovery(@Observes BeforeBeanDiscovery event)
   {
      boolean problems = false;
      StringBuilder problemString = new StringBuilder();

      List<Class<? extends XmlDocumentProvider>> providers = getDocumentProviders();
      for (Class<? extends XmlDocumentProvider> cl : providers)
      {
         try
         {
            XmlDocumentProvider provider = cl.newInstance();
            provider.open();
            XmlDocument d;
            while ((d = provider.getNextDocument()) != null)
            {
               ParserMain parser = new ParserMain();
               ModelBuilder builder = new ModelBuilder();
               SaxNode parentNode = parser.parse(d.getInputSource(), d.getFileUrl());
               ;
               results.add(builder.build(parentNode));
            }
         }
         catch (Exception e)
         {
            throw new RuntimeException(e);
         }
      }

      for (XmlResult r : results)
      {
         if (!r.getProblems().isEmpty())
         {
            problems = true;
            for (String i : r.getProblems())
            {
               problemString.append(i);
               problemString.append("\n");
            }
         }
         for (BeanResult b : r.getFieldValues().keySet())
         {
            int val = count++;
            fieldValues.put(val, r.getFieldValues().get(b));
            Map<String, Object> am = new HashMap<String, Object>();
            am.put("value", val);
            Annotation a = ac.get(XmlId.class, am);
            b.getBuilder().addToClass(a);
         }

         for (Class b : r.getQualifiers())
         {
            event.addQualifier(b);
         }
         for (Class b : r.getInterceptorBindings())
         {
            event.addInterceptorBinding(b);
         }
         for (Entry<Class<? extends Annotation>, Annotation[]> b : r.getStereotypes().entrySet())
         {
            event.addStereotype(b.getKey(), b.getValue());
         }
         for (BeanResult bb : r.getBeans())
         {
            boolean install = true;
            for (Object className : bb.getDependencies())
            {
               try
               {
                  bb.getType().getClassLoader().loadClass(className.toString());
               }
               catch (ClassNotFoundException e)
               {
                  install = false;
                  break;
               }
            }
            if (install)
            {
               AnnotatedType tp = bb.getBuilder().create();
               event.addAnnotatedType(tp);
               types.put(tp.getJavaClass(), tp);
            }
         }
         veto.addAll(r.getVeto());

      }
      if (problems)
      {
         throw new RuntimeException(problemString.toString());
      }
   }

   public void processAnotated(@Observes ProcessAnnotatedType event)
   {
      // veto implementation
      if (veto.contains(event.getAnnotatedType().getJavaClass()))
      {
         event.veto();
      }
   }

   public void processInjectionTarget(@Observes ProcessInjectionTarget event)
   {

      AnnotatedType at = event.getAnnotatedType();
      XmlId xid = at.getAnnotation(XmlId.class);
      if (xid != null)
      {
         List<FieldValueObject> fvs = fieldValues.get(xid.value());
         event.setInjectionTarget(new InjectionTargetWrapper(event.getInjectionTarget(), fvs));
      }

   }

   public List<Class<? extends XmlDocumentProvider>> getDocumentProviders()
   {
      List<Class<? extends XmlDocumentProvider>> ret = new ArrayList<Class<? extends XmlDocumentProvider>>();
      try
      {
         Enumeration<URL> urls = getClass().getClassLoader().getResources(PROVIDERS_FILE);
         while (urls.hasMoreElements())
         {

            URL u = urls.nextElement();
            String data = FileDataReader.readUrl(u);
            String[] providers = data.split("\\s");
            for (String provider : providers)
            {
               Class res = null;
               try
               {
                  res = getClass().getClassLoader().loadClass(provider);
               }
               catch (ClassNotFoundException e)
               {
                  res = Thread.currentThread().getContextClassLoader().loadClass(provider);
               }
               if (res == null)
               {
                  throw new RuntimeException("Could not load XML configuration provider " + provider + " configured in file " + u.toString());
               }
               ret.add(res);
            }
         }
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
      return ret;
   }

}
