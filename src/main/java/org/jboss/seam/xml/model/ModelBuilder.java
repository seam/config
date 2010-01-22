/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.enterprise.inject.Stereotype;
import javax.inject.Qualifier;
import javax.interceptor.InterceptorBinding;

import org.jboss.seam.xml.core.BeanResult;
import org.jboss.seam.xml.core.XmlResult;
import org.jboss.seam.xml.fieldset.FieldValueObject;
import org.jboss.seam.xml.parser.SaxNode;
import org.jboss.seam.xml.parser.namespace.InvalidElementException;
import org.jboss.seam.xml.parser.namespace.NamespaceElementResolver;
import org.jboss.seam.xml.parser.namespace.PackageNamespaceElementResolver;
import org.jboss.seam.xml.parser.namespace.RootNamespaceElementResolver;
import org.jboss.seam.xml.util.XmlObjectConverter;
import org.jboss.weld.extensions.util.AnnotationInstanceProvider;
import org.jboss.weld.extensions.util.annotated.NewAnnotatedTypeBuilder;
import org.w3c.dom.DOMException;

/**
 * Parser for xml configration
 * 
 * @author stuart
 * 
 */
public class ModelBuilder
{

   AnnotationInstanceProvider ac = new AnnotationInstanceProvider();

   static final String ROOT_NAMESPACE = "urn:seam:core";

   Map<String, NamespaceElementResolver> resolvers;

   public XmlResult build(SaxNode root)
   {

      resolvers = new HashMap<String, NamespaceElementResolver>();

      XmlResult ret = new XmlResult();

      if (!root.getName().equals("Beans"))
      {
         throw new RuntimeException("Wrong root element for XML config file, expected:<Beans> found:" + root.getName());
      }
      if (!ROOT_NAMESPACE.equals(root.getNamespaceUri()))
      {
         throw new RuntimeException("Wrong root namespace for XML config file, expected:" + ROOT_NAMESPACE + " found:" + root.getNamespaceUri());
      }

      resolvers.put(ROOT_NAMESPACE, new RootNamespaceElementResolver());

      List<SaxNode> children = root.getChildren();
      for (SaxNode node : children)
      {
         try
         {
            // nodes with a null namespace are whitespace nodes etc
            if (node.getNamespaceUri() != null)
            {
               XmlItem rb = resolveNode(node, null);
               addNodeToResult(ret, rb);
            }
         }
         catch (Exception e)
         {
            ret.addProblem(e.getMessage());
            e.printStackTrace();
         }
      }

      return ret;
   }

   @SuppressWarnings("unchecked")
   private void addNodeToResult(XmlResult ret, XmlItem rb) throws InvalidElementException
   {

      if (rb.getType() == XmlItemType.CLASS || rb.getType() == XmlItemType.ANNOTATION)
      {
         ResultType type = getItemType(rb);
         if (type == ResultType.BEAN)
         {
            BeanResult<?> tp = buildAnnotatedType(rb);
            ret.getBeans().add(tp);
            List<FieldValueObject> fields = new ArrayList<FieldValueObject>();
            for (XmlItem xi : rb.getChildren())
            {
               if (xi.getType() == XmlItemType.FIELD)
               {
                  FieldValueObject f = xi.getFieldValue();
                  if (f != null)
                  {
                     fields.add(f);
                  }
               }
            }
            if (!fields.isEmpty())
            {
               ret.getFieldValues().put(tp, fields);
            }

         }
         else if (type == ResultType.QUALIFIER)
         {
            ret.getQualifiers().add(rb.getJavaClass());
         }
         else if (type == ResultType.INTERCEPTOR_BINDING)
         {
            ret.getInterceptorBindings().add(rb.getJavaClass());
         }
         else if (type == ResultType.STEREOTYPE)
         {
            addSteriotypeToResult(ret, rb);
         }
      }
      else if (rb.getType() == XmlItemType.VETO)
      {
         for (XmlItem it : rb.getChildren())
         {
            ret.addVeto(it.getJavaClass());
         }
      }
   }

   protected XmlItem resolveNode(SaxNode node, XmlItem parent) throws DOMException, InvalidElementException
   {
      NamespaceElementResolver resolver = resolveNamepsace(node.getNamespaceUri());

      Map<String, String> attributes = node.getAttributes();
      String innerText = node.getInnerText().trim();
      if (innerText.equals(""))
      {
         innerText = null;
      }
      XmlItem ret = resolver.getItemForNamespace(node.getName(), parent, innerText, attributes);

      if (ret == null)
      {
         throw new InvalidElementException("Could not resolve node " + node.getName() + " in namespace " + node.getNamespaceUri());
      }
      List<SaxNode> children = node.getChildren();
      for (SaxNode n : children)
      {
         if (n.getNamespaceUri() != null)
         {
            XmlItem rb = resolveNode(n, ret);
            ret.addChild(rb);
         }
      }
      ret.resolveChildren();
      return ret;

   }

   protected NamespaceElementResolver resolveNamepsace(String namespaceURI)
   {
      if (resolvers.containsKey(namespaceURI))
      {
         return resolvers.get(namespaceURI);
      }
      String ns = namespaceURI.replaceFirst("urn:java:", "");
      PackageNamespaceElementResolver res = new PackageNamespaceElementResolver(ns);
      resolvers.put(namespaceURI, res);
      return res;
   }

   /**
    * Determines the type of an element by examining its child nodes
    */
   protected ResultType getItemType(XmlItem item) throws InvalidElementException
   {

      ResultType ret = null;
      for (XmlItem it : item.getChildren())
      {
         if (it.getType() == XmlItemType.ANNOTATION)
         {
            if (it.getJavaClass() == InterceptorBinding.class)
            {
               if (ret != null)
               {
                  throw new InvalidElementException("Element cannot be both an INTERCEPTOR_BINDING and a " + ret.toString());
               }
               else
               {
                  ret = ResultType.INTERCEPTOR_BINDING;
               }
            }
            else if (it.getJavaClass() == Qualifier.class)
            {
               if (ret != null)
               {
                  throw new InvalidElementException("Element cannot be both an QUALIFIER and a " + ret.toString());
               }
               else
               {
                  ret = ResultType.QUALIFIER;
               }
            }
            else if (it.getJavaClass() == Stereotype.class)
            {
               if (ret != null)
               {
                  throw new InvalidElementException("Element cannot be both an STEREOTYPE and a " + ret.toString());
               }
               else
               {
                  ret = ResultType.STEREOTYPE;
               }
            }
         }
         else if (it.getType() == XmlItemType.VETO)
         {
            if (ret != null)
            {
               throw new InvalidElementException("Element cannot be both an VETO and a " + ret.toString());
            }
            else
            {
               ret = ResultType.VETO;
            }
         }

      }
      if (ret == null)
      {
         ret = ResultType.BEAN;
      }
      return ret;
   }

   @SuppressWarnings("unchecked")
   <T> BeanResult<T> buildAnnotatedType(XmlItem rb) throws InvalidElementException
   {
      BeanResult<T> result = new BeanResult<T>(rb.getJavaClass());
      NewAnnotatedTypeBuilder<T> type = result.getBuilder();
      // list of constructor arguments
      List<XmlItem> constList = new ArrayList<XmlItem>();
      for (XmlItem item : rb.getChildren())
      {
         if (item.getType() == XmlItemType.ANNOTATION)
         {

            Annotation a = createAnnotation(item);
            type.addToClass(a);
         }
         else if (item.getType() == XmlItemType.CLASS)
         {
            constList.add(item);

         }
         else if (item.getType() == XmlItemType.FIELD)
         {
            for (XmlItem fi : item.getChildren())
            {
               if (fi.getType() == XmlItemType.ANNOTATION)
               {
                  Annotation a = createAnnotation(fi);
                  type.addToField(item.getField(), a);
               }
            }
         }
         else if (item.getType() == XmlItemType.METHOD)
         {
            int paramCount = 0;
            for (XmlItem fi : item.getChildren())
            {
               if (fi.getType() == XmlItemType.ANNOTATION)
               {

                  // TODO: pass in attribute map
                  Annotation a = createAnnotation(fi);
                  type.addToMethod(item.getMethod(), a);
               }
               else if (fi.getType() == XmlItemType.CLASS)
               {
                  int param = paramCount++;
                  for (XmlItem pan : fi.getChildren())
                  {
                     if (pan.getType() == XmlItemType.ANNOTATION)
                     {
                        Annotation a = createAnnotation(pan);
                        type.addToMethodParameter(item.getMethod(), param, a);
                     }
                     else
                     {
                        throw new RuntimeException("Method parameters may only have annotations as children in " + item.getJavaClass().getName());
                     }
                  }
               }
            }
         }
         else if (item.getType() == XmlItemType.DEPENDENCY)
         {
            result.getDependencies().add(item.getInnerText());
         }
      }
      if (!constList.isEmpty())
      {
         // the bean defined constructor arguments
      }
      return result;
   }

   @SuppressWarnings("unchecked")
   void addSteriotypeToResult(XmlResult ret, XmlItem rb) throws InvalidElementException
   {

      Annotation[] values = new Annotation[rb.getChildren().size()];
      int count = 0;
      for (XmlItem item : rb.getChildren())
      {
         if (item.getType() == XmlItemType.ANNOTATION)
         {
            Annotation a = createAnnotation(item);
            values[count] = a;
         }
         else
         {
            throw new InvalidElementException("Setereotype " + rb.getJavaClass() + " has an item that does not represent an annotation in its XML configurations");
         }
         count++;
      }
      ret.getStereotypes().put(rb.getJavaClass(), values);

   }

   @SuppressWarnings("unchecked")
   Annotation createAnnotation(XmlItem item) throws InvalidElementException
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
            throw new InvalidElementException("Annotation " + item.getJavaClass().getName() + " does not have a member named " + mname + " ,error in XML");
         }
         Class<?> returnType = m.getReturnType();
         typedVars.put(mname, XmlObjectConverter.convert(returnType, e.getValue()));
      }

      return ac.get(item.getJavaClass(), typedVars);
   }

   public void validateXmlItem(XmlItem item)
   {
      Set<XmlItemType> allowed = item.getAllowedItem();
      for (XmlItem i : item.getChildren())
      {
         if (!allowed.contains(item.getType()))
         {
            throw new RuntimeException("Item " + item.getType() + " is not allowed to contain " + i.getType());
         }
         validateXmlItem(i);
      }
   }

}
