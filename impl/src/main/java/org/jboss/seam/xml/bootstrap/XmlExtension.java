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
package org.jboss.seam.xml.bootstrap;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
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
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.AnnotatedConstructor;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedParameter;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessInjectionTarget;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Named;

import org.jboss.seam.xml.annotations.internal.ApplyQualifiers;
import org.jboss.seam.xml.core.BeanResult;
import org.jboss.seam.xml.core.BeanResultType;
import org.jboss.seam.xml.core.GenericBeanResult;
import org.jboss.seam.xml.core.XmlConfiguredBean;
import org.jboss.seam.xml.core.XmlId;
import org.jboss.seam.xml.core.XmlResult;
import org.jboss.seam.xml.fieldset.FieldValueObject;
import org.jboss.seam.xml.fieldset.InjectionTargetWrapper;
import org.jboss.seam.xml.model.ModelBuilder;
import org.jboss.seam.xml.parser.ParserMain;
import org.jboss.seam.xml.parser.SaxNode;
import org.jboss.seam.xml.util.FileDataReader;
import org.jboss.weld.extensions.annotated.AnnotatedTypeBuilder;
import org.jboss.weld.extensions.core.Exact;
import org.jboss.weld.extensions.core.Veto;
import org.jboss.weld.extensions.util.AnnotationInstanceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlExtension implements Extension
{

   AnnotationInstanceProvider ac = new AnnotationInstanceProvider();

   static final String PROVIDERS_FILE = "META-INF/services/org.jboss.seam.xml.XmlExtension";

   List<XmlResult> results = new ArrayList<XmlResult>();

   Set<Class<?>> veto = new HashSet<Class<?>>();

   int count = 0;

   private static final Logger log = LoggerFactory.getLogger(XmlExtension.class);

   /**
    * map of syntetic bean id to a list of field value objects
    */
   Map<Integer, List<FieldValueObject>> fieldValues = new HashMap<Integer, List<FieldValueObject>>();

   List<Exception> errors = new ArrayList<Exception>();

   Map<Class<?>, GenericBeanResult> genericBeans = new HashMap<Class<?>, GenericBeanResult>();

   /**
    * This is the entry point for the extension
    */
   public void beforeBeanDiscovery(@Observes BeforeBeanDiscovery event, BeanManager beanManager)
   {
      log.info("Seam XML Config Starting...");
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
         for (GenericBeanResult b : r.getGenericBeans())
         {
            genericBeans.put(b.getGenericBean(), b);
         }

         // add the qualifiers as we need them before we process the generic
         // bean info
         for (Class<? extends Annotation> b : r.getQualifiers())
         {
            log.info("Adding XML Defined Qualifier: " + b.getName());
            event.addQualifier(b);
         }
      }

      for (XmlResult r : results)
      {
         if (!r.getProblems().isEmpty())
         {
            for (String i : r.getProblems())
            {
               errors.add(new RuntimeException(i));
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
               Annotation a = ac.get(XmlId.class, am);
               b.getBuilder().addToClass(a);
            }
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
         for (BeanResult<?> bb : r.getFlattenedBeans())
         {
            GenericBeanResult found = null;
            for (Class<?> g : genericBeans.keySet())
            {
               if (g.isAssignableFrom(bb.getType()))
               {
                  found = genericBeans.get(g);
                  break;
               }
            }
            if (found != null)
            {
               List<AnnotatedType<?>> types = processGenericBeans(bb, found, beanManager);
               for (AnnotatedType<?> i : types)
               {
                  event.addAnnotatedType(i);
               }
            }

            bb.getBuilder().addToClass(new AnnotationLiteral<XmlConfiguredBean>()
            {
            });
            AnnotatedType<?> tp = fixExactSupport(bb.getBuilder()).create();
            log.info("Adding XML Defined Bean: " + tp.getJavaClass().getName());
            event.addAnnotatedType(tp);

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

   public <T> void processInjectionTarget(@Observes ProcessInjectionTarget<T> event)
   {

      AnnotatedType<T> at = event.getAnnotatedType();
      XmlId xid = at.getAnnotation(XmlId.class);
      if (xid != null)
      {
         log.info("Wrapping InjectionTarget to set field values: " + event.getAnnotatedType().getJavaClass().getName());
         List<FieldValueObject> fvs = fieldValues.get(xid.value());
         event.setInjectionTarget(new InjectionTargetWrapper<T>(event.getInjectionTarget(), fvs));
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

   /**
    * installs a set of secondary beans for a given generic bean, the secondary
    * beans have the same qualifiers added to them as the generic bean, in
    * addition the generic beans qualifiers are added whereever the
    * ApplyQualiers annotation is found
    * 
    */
   public List<AnnotatedType<?>> processGenericBeans(BeanResult<?> bean, GenericBeanResult genericBeans, BeanManager beanManager)
   {
      List<AnnotatedType<?>> ret = new ArrayList<AnnotatedType<?>>();
      AnnotatedType<?> rootType = bean.getBuilder().create();
      // ret.add(rootType);
      Set<Annotation> qualifiers = new HashSet<Annotation>();

      for (Annotation i : rootType.getAnnotations())
      {
         if (beanManager.isQualifier(i.annotationType()))
         {
            qualifiers.add(i);
         }
      }
      for (BeanResult<?> c : genericBeans.getSecondaryBeans())
      {

         AnnotatedType<?> type = c.getBuilder().create();
         AnnotatedTypeBuilder<?> gb = new AnnotatedTypeBuilder().setJavaClass(type.getJavaClass());
         if (c.getBeanType() == BeanResultType.MODIFIES)
         {
            gb.readFromType((Class) type.getJavaClass());
            // we don't want to keep the veto annotation on the class
            gb.removeFromClass(Veto.class);
         }
         // if the original type was qualified @Default we add that as well
         if (!isQualifierPresent(type, beanManager))
         {
            gb.addToClass(new DefaultLiteral());
         }
         // we always apply qualifiers to the actual type
         for (Annotation q : qualifiers)
         {
            gb.addToClass(q);
         }
         for (AnnotatedField<?> f : type.getFields())
         {
            if (f.isAnnotationPresent(ApplyQualifiers.class))
            {
               // we need to manually add @default as it stops being applied
               // when
               // we add our qualifiers, however if we are deling with the main
               // type we do not
               // bother, as it should not be qualified @Default
               if (!isQualifierPresent(f, beanManager) && f.getJavaMember().getType() != rootType.getJavaClass())
               {
                  gb.addToField(f.getJavaMember(), new DefaultLiteral());
               }
               for (Annotation q : qualifiers)
               {
                  gb.addToField(f.getJavaMember(), q);
               }
            }
         }
         for (AnnotatedMethod<?> m : type.getMethods())
         {

            if (m.isAnnotationPresent(ApplyQualifiers.class))
            {
               if (!isQualifierPresent(m, beanManager))
               {
                  gb.addToMethod(m.getJavaMember(), new DefaultLiteral());
               }
               for (Annotation q : qualifiers)
               {
                  gb.addToMethod(m.getJavaMember(), q);
               }
            }

            for (AnnotatedParameter<?> p : m.getParameters())
            {

               if (p.isAnnotationPresent(ApplyQualifiers.class))
               {
                  if (!isQualifierPresent(p, beanManager) && p.getBaseType() != rootType.getJavaClass())
                  {
                     gb.addToMethodParameter(m.getJavaMember(), p.getPosition(), new DefaultLiteral());
                  }
                  for (Annotation q : qualifiers)
                  {
                     gb.addToMethodParameter(m.getJavaMember(), p.getPosition(), q);
                  }
               }
            }
         }

         for (AnnotatedConstructor<?> con : type.getConstructors())
         {
            if (con.isAnnotationPresent(ApplyQualifiers.class))
            {
               if (!isQualifierPresent(con, beanManager))
               {
                  gb.addToConstructor((Constructor) con.getJavaMember(), new DefaultLiteral());
               }
               for (Annotation q : qualifiers)
               {
                  gb.addToConstructor((Constructor) con.getJavaMember(), q);
               }
            }

            for (AnnotatedParameter<?> p : con.getParameters())
            {
               if (p.isAnnotationPresent(ApplyQualifiers.class) && p.getBaseType() != rootType.getJavaClass())
               {
                  if (!isQualifierPresent(p, beanManager))
                  {
                     gb.addToConstructorParameter((Constructor) con.getJavaMember(), p.getPosition(), new DefaultLiteral());
                  }
                  for (Annotation q : qualifiers)
                  {
                     gb.addToConstructorParameter((Constructor) con.getJavaMember(), p.getPosition(), q);
                  }
               }
            }
         }

         ret.add(fixExactSupport(gb).create());
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

   /**
    * temprary hack to support @Exact in seam-xml, remove once WELD-485 is
    * resolved
    */
   public <X> AnnotatedTypeBuilder<X> fixExactSupport(AnnotatedTypeBuilder<X> old)
   {
      AnnotatedType<X> pat = old.create();
      AnnotatedTypeBuilder<X> builder = new AnnotatedTypeBuilder<X>().readFromType(pat);
      // support for @Exact
      // fields
      for (AnnotatedField<? super X> f : pat.getFields())
      {
         if (f.isAnnotationPresent(Exact.class))
         {
            Class<?> type = f.getAnnotation(Exact.class).value();
            builder.overrideFieldType(f.getJavaMember(), type);
         }
      }
      // method parameters
      for (AnnotatedMethod<? super X> m : pat.getMethods())
      {
         for (AnnotatedParameter<? super X> p : m.getParameters())
         {
            if (p.isAnnotationPresent(Exact.class))
            {
               Class<?> type = p.getAnnotation(Exact.class).value();
               builder.overrideMethodParameterType(m.getJavaMember(), p.getPosition(), type);
            }
         }
      }
      // constructor parameters
      for (AnnotatedConstructor<X> c : pat.getConstructors())
      {
         for (AnnotatedParameter<? super X> p : c.getParameters())
         {
            if (p.isAnnotationPresent(Exact.class))
            {
               Class<?> type = p.getAnnotation(Exact.class).value();
               builder.overrideConstructorParameterType(c.getJavaMember(), p.getPosition(), type);
            }
         }
      }
      return builder;
   }

   public static class DefaultLiteral extends AnnotationLiteral<Default> implements Default
   {
   };
}
