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
package org.jboss.seam.config.xml.bootstrap;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessInjectionTarget;
import javax.inject.Named;

import org.jboss.logging.Logger;
import org.jboss.seam.config.xml.core.BeanResult;
import org.jboss.seam.config.xml.core.XmlConfiguredBean;
import org.jboss.seam.config.xml.core.XmlId;
import org.jboss.seam.config.xml.core.XmlResult;
import org.jboss.seam.config.xml.fieldset.FieldValueObject;
import org.jboss.seam.config.xml.fieldset.InjectionTargetWrapper;
import org.jboss.seam.config.xml.model.ModelBuilder;
import org.jboss.seam.config.xml.parser.ParserMain;
import org.jboss.seam.config.xml.parser.SaxNode;
import org.jboss.seam.config.xml.util.FileDataReader;
import org.jboss.seam.solder.literal.DefaultLiteral;
import org.jboss.seam.solder.reflection.AnnotationInstanceProvider;

public class XmlConfigExtension implements Extension
{

   private AnnotationInstanceProvider annotationInstanceProvider = new AnnotationInstanceProvider();

   static final String PROVIDERS_FILE = "META-INF/services/" + XmlDocumentProvider.class.getName();

   private List<XmlResult> results = new ArrayList<XmlResult>();

   private Set<Class<?>> veto = new HashSet<Class<?>>();

   private int count = 0;

   private static final Logger log = Logger.getLogger(XmlConfigExtension.class);

   /**
    * map of syntetic bean id to a list of field value objects
    */
   private Map<Integer, List<FieldValueObject>> fieldValues = new HashMap<Integer, List<FieldValueObject>>();

   private List<Exception> errors = new ArrayList<Exception>();

   /**
    * This is the entry point for the extension
    */
   public void beforeBeanDiscovery(@Observes BeforeBeanDiscovery event, BeanManager beanManager)
   {
      log.info("Seam Config XML provider starting...");
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
               log.info("Reading XML file: " + d.getFileUrl());
               ParserMain parser = new ParserMain();
               ModelBuilder builder = new ModelBuilder(d.getFileUrl());
               SaxNode parentNode = parser.parse(d.getInputSource(), d.getFileUrl(), errors);
               results.add(builder.build(parentNode, beanManager));
            }
         }
         catch (Exception e)
         {
            errors.add(e);
         }
      }
      // we sort the results so the beans are always installed in the same order
      Collections.sort(results);
      // build the generic bean data
      for (XmlResult r : results)
      {
         // add the qualifiers etc first
         for (Class<? extends Annotation> b : r.getQualifiers())
         {
            log.info("Adding XML Defined Qualifier: " + b.getName());
            event.addQualifier(b);
         }
         for (Class<? extends Annotation> b : r.getInterceptorBindings())
         {
            log.info("Adding XML Defined Interceptor Binding: " + b.getName());
            event.addInterceptorBinding(b);
         }
         for (Entry<Class<? extends Annotation>, Annotation[]> b : r.getStereotypes().entrySet())
         {
            log.info("Adding XML Defined Stereotype: " + b.getKey().getName());
            event.addStereotype(b.getKey(), b.getValue());
         }
      }

      for (XmlResult r : results)
      {
         if (!r.getProblems().isEmpty())
         {
            for (String i : r.getProblems())
            {
               errors.add(new Exception(i));
            }
         }
         for (BeanResult<?> b : r.getFlattenedBeans())
         {
            if (!b.getFieldValues().isEmpty())
            {
               int val = count++;
               fieldValues.put(val, b.getFieldValues());
               Map<String, Object> am = new HashMap<String, Object>();
               am.put("value", val);
               Annotation a = annotationInstanceProvider.get(XmlId.class, am);
               b.addToClass(a);
            }
         }

         for (BeanResult<?> bb : r.getFlattenedBeans())
         {
            AnnotatedType<?> tp = bb.getAnnotatedType();
            log.info("Adding XML Defined Bean: " + tp.getJavaClass().getName());
            ProcessAnnotatedType<?> pat = new ProcessAnnotatedTypeImpl((AnnotatedType) tp);
            beanManager.fireEvent(pat, DefaultLiteral.INSTANCE);
            event.addAnnotatedType(pat.getAnnotatedType());
         }

         veto.addAll(r.getVeto());

      }
   }

   public <T> void processAnotated(@Observes ProcessAnnotatedType<T> event, BeanManager manager)
   {
      if (event.getAnnotatedType().isAnnotationPresent(XmlConfiguredBean.class))
      {
         return;
      }
      // veto implementation
      if (veto.contains(event.getAnnotatedType().getJavaClass()))
      {
         log.info("Preventing installation of default bean: " + event.getAnnotatedType().getJavaClass().getName());
         event.veto();
         return;
      }
   }

   public <T> void processInjectionTarget(@Observes ProcessInjectionTarget<T> event, BeanManager manager)
   {

      AnnotatedType<T> at = event.getAnnotatedType();
      XmlId xid = at.getAnnotation(XmlId.class);
      if (xid != null)
      {
         log.info("Wrapping InjectionTarget to set field values: " + event.getAnnotatedType().getJavaClass().getName());
         List<FieldValueObject> fvs = fieldValues.get(xid.value());
         event.setInjectionTarget(new InjectionTargetWrapper<T>(event.getInjectionTarget(), fvs, manager));
      }
   }

   public void processAfterBeanDeployment(@Observes AfterBeanDiscovery event)
   {
      for (Exception t : errors)
      {
         event.addDefinitionError(t);
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
               log.info("Loading XmlDocumentProvider: " + provider);
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

   public boolean isQualifierPresent(Annotated f, BeanManager beanManager)
   {
      for (Annotation a : f.getAnnotations())
      {
         if (a.annotationType().equals(Named.class))
         {
            continue;
         }
         if (beanManager.isQualifier(a.annotationType()))
         {
            return true;
         }
      }
      return false;
   }

}
