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
import org.jboss.seam.xml.parser.namespace.CompositeNamespaceElementResolver;
import org.jboss.seam.xml.parser.namespace.NamespaceElementResolver;
import org.jboss.seam.xml.parser.namespace.RootNamespaceElementResolver;
import org.jboss.seam.xml.util.XmlConfigurationException;
import org.jboss.seam.xml.util.XmlObjectConverter;
import org.jboss.weld.extensions.util.AnnotationInstanceProvider;
import org.jboss.weld.extensions.util.annotated.NewAnnotatedTypeBuilder;

/**
 * Builds an XML result from sax nodes
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
         throw new XmlConfigurationException("Wrong root element for XML config file, expected:<Beans> found:" + root.getName(), root.getDocument(), root.getLineNo());
      }
      if (!ROOT_NAMESPACE.equals(root.getNamespaceUri()))
      {
         throw new XmlConfigurationException("Wrong root namespace for XML config file, expected:" + ROOT_NAMESPACE + " found:" + root.getNamespaceUri(), root.getDocument(), root.getLineNo());
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
               // validateXmlItem(rb);
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
   private void addNodeToResult(XmlResult ret, XmlItem rb)
   {

      if (rb.getType() == XmlItemType.CLASS || rb.getType() == XmlItemType.ANNOTATION)
      {
         ResultType type = getItemType(rb);
         if (type == ResultType.BEAN)
         {
            BeanResult<?> tp = buildAnnotatedType(rb);
            if (tp.isExtend())
            {
               ret.getExtendBeans().add(tp);
            }
            else
            {
               ret.getBeans().add(tp);
            }
            if (tp.isOverride())
            {
               ret.addVeto(tp.getType());
            }
            List<FieldValueObject> fields = new ArrayList<FieldValueObject>();
            for (FieldXmlItem xi : rb.getChildrenOfType(FieldXmlItem.class))
            {
               FieldValueObject f = xi.getFieldValue();
               if (f != null)
               {
                  fields.add(f);
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
            addStereotypeToResult(ret, rb);
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

   protected XmlItem resolveNode(SaxNode node, XmlItem parent)
   {
      NamespaceElementResolver resolver = resolveNamepsace(node.getNamespaceUri());

      XmlItem ret = resolver.getItemForNamespace(node, parent);

      if (ret == null)
      {
         throw new XmlConfigurationException("Could not resolve node " + node.getName() + " in namespace " + node.getNamespaceUri(), node.getDocument(), node.getLineNo());
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
      CompositeNamespaceElementResolver res = new CompositeNamespaceElementResolver(ns.split(":"));
      resolvers.put(namespaceURI, res);
      return res;
   }

   /**
    * Determines the type of an element by examining its child nodes
    */
   protected ResultType getItemType(XmlItem item)
   {

      ResultType ret = null;
      for (AnnotationXmlItem it : item.getChildrenOfType(AnnotationXmlItem.class))
      {
         if (it.getJavaClass() == InterceptorBinding.class)
         {
            if (ret != null)
            {
               throw new XmlConfigurationException("Element cannot be both an INTERCEPTOR_BINDING and a " + ret.toString(), item.getDocument(), item.getLineno());
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
               throw new XmlConfigurationException("Element cannot be both an QUALIFIER and a " + ret.toString(), item.getDocument(), item.getLineno());
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
               throw new XmlConfigurationException("Element cannot be both an STEREOTYPE and a " + ret.toString(), item.getDocument(), item.getLineno());
            }
            else
            {
               ret = ResultType.STEREOTYPE;
            }
         }
      }
      for (VetoXmlItem it : item.getChildrenOfType(VetoXmlItem.class))
      {
         if (ret != null)
         {
            throw new XmlConfigurationException("Element cannot be both an VETO and a " + ret.toString(), item.getDocument(), item.getLineno());
         }
         else
         {
            ret = ResultType.VETO;
         }
      }
      if (ret == null)
      {
         ret = ResultType.BEAN;
      }
      return ret;
   }

   @SuppressWarnings("unchecked")
   <T> BeanResult<T> buildAnnotatedType(XmlItem rb)
   {
      BeanResult<T> result = new BeanResult<T>(rb.getJavaClass());
      NewAnnotatedTypeBuilder<T> type = result.getBuilder();
      // list of constructor arguments
      List<XmlItem> constList = new ArrayList<XmlItem>();

      boolean override = !rb.getChildrenOfType(OverrideXmlItem.class).isEmpty();
      boolean extend = !rb.getChildrenOfType(ExtendsXmlItem.class).isEmpty();
      if (override && extend)
      {
         throw new XmlConfigurationException("A bean may not both <override> and <extend> an existing bean", rb.getDocument(), rb.getLineno());
      }
      result.setOverride(override);
      result.setExtend(extend);

      for (AnnotationXmlItem item : rb.getChildrenOfType(AnnotationXmlItem.class))
      {
         Annotation a = createAnnotation(item);
         type.addToClass(a);
      }

      for (ParameterXmlItem item : rb.getChildrenOfType(ParameterXmlItem.class))
      {
         constList.add(item);
      }
      for (FieldXmlItem item : rb.getChildrenOfType(FieldXmlItem.class))
      {
         for (AnnotationXmlItem fi : item.getChildrenOfType(AnnotationXmlItem.class))
         {
            Annotation a = createAnnotation(fi);
            type.addToField(item.getField(), a);
         }
      }
      for (MethodXmlItem item : rb.getChildrenOfType(MethodXmlItem.class))
      {
         int paramCount = 0;

         for (AnnotationXmlItem fi : item.getChildrenOfType(AnnotationXmlItem.class))
         {
            Annotation a = createAnnotation(fi);
            type.addToMethod(item.getMethod(), a);
         }
         for (ParameterXmlItem fi : item.getChildrenOfType(ParameterXmlItem.class))
         {
            int param = paramCount++;
            for (AnnotationXmlItem pan : fi.getChildrenOfType(AnnotationXmlItem.class))
            {
               Annotation a = createAnnotation(pan);
               type.addToMethodParameter(item.getMethod(), param, a);
            }
         }

      }

      if (!constList.isEmpty())
      {
         // the bean defined constructor arguments
      }
      return result;
   }

   @SuppressWarnings("unchecked")
   void addStereotypeToResult(XmlResult ret, XmlItem rb)
   {

      Annotation[] values = new Annotation[rb.getChildren().size()];
      int count = 0;
      for (XmlItem item : rb.getChildren())
      {
         if (item.getType() == XmlItemType.ANNOTATION)
         {
            Annotation a = createAnnotation((AnnotationXmlItem) item);
            values[count] = a;
         }
         else
         {
            throw new XmlConfigurationException("Setereotype " + rb.getJavaClass() + " has an item that does not represent an annotation in its XML configurations", rb.getDocument(), rb.getLineno());
         }
         count++;
      }
      ret.getStereotypes().put(rb.getJavaClass(), values);

   }

   @SuppressWarnings("unchecked")
   Annotation createAnnotation(AnnotationXmlItem item)
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

      return ac.get((Class) item.getJavaClass(), typedVars);
   }

   public void validateXmlItem(XmlItem item)
   {
      Set<XmlItemType> allowed = item.getAllowedItem();
      for (XmlItem i : item.getChildren())
      {
         if (!allowed.contains(item.getType()))
         {
            throw new XmlConfigurationException("Item " + item.getType() + " is not allowed to contain " + i.getType(), item.getDocument(), item.getLineno());
         }
         validateXmlItem(i);
      }
   }

}
