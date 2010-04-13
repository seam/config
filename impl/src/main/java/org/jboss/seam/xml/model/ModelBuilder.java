/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.enterprise.inject.Stereotype;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.inject.Qualifier;
import javax.interceptor.InterceptorBinding;

import org.jboss.seam.xml.core.BeanResult;
import org.jboss.seam.xml.core.GenericBeanResult;
import org.jboss.seam.xml.core.XmlResult;
import org.jboss.seam.xml.fieldset.FieldValueObject;
import org.jboss.seam.xml.parser.SaxNode;
import org.jboss.seam.xml.parser.namespace.CompositeNamespaceElementResolver;
import org.jboss.seam.xml.parser.namespace.NamespaceElementResolver;
import org.jboss.seam.xml.parser.namespace.RootNamespaceElementResolver;
import org.jboss.seam.xml.util.XmlConfigurationException;
import org.jboss.seam.xml.util.XmlObjectConverter;
import org.jboss.weld.extensions.util.AnnotationInstanceProvider;
import org.jboss.weld.extensions.util.ReflectionUtils;
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

   static final String ROOT_NAMESPACE = "urn:java:seam:core";

   static final String BEANS_ROOT_NAMESPACE = "http://java.sun.com/xml/ns/javaee";

   Map<String, NamespaceElementResolver> resolvers;

   public XmlResult build(SaxNode root)
   {

      resolvers = new HashMap<String, NamespaceElementResolver>();

      XmlResult ret = new XmlResult();

      if (!root.getName().equals("beans"))
      {
         throw new XmlConfigurationException("Wrong root element for XML config file, expected:<beans> found:" + root.getName(), root.getDocument(), root.getLineNo());
      }
      if (!(ROOT_NAMESPACE.equals(root.getNamespaceUri()) || BEANS_ROOT_NAMESPACE.equals(root.getNamespaceUri())))
      {
         throw new XmlConfigurationException("Wrong root namespace for XML config file, expected:" + ROOT_NAMESPACE + " or " + BEANS_ROOT_NAMESPACE + " found:" + root.getNamespaceUri(), root.getDocument(), root.getLineNo());
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
               if (node.getNamespaceUri().equals(BEANS_ROOT_NAMESPACE))
               {
                  continue;
               }
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
            BeanResult<?> tp = buildAnnotatedType((ClassXmlItem) rb);
            if (rb.getJavaClass().isInterface())
            {
               ret.addInterface(tp);
            }
            else
            {
               ret.addBean(tp);
            }
            if (tp.isOverride() || tp.isExtend())
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
            for (PropertyXmlItem xi : rb.getChildrenOfType(PropertyXmlItem.class))
            {
               FieldValueObject f = xi.getFieldValue();
               if (f != null)
               {
                  fields.add(f);
               }
            }
            if (!fields.isEmpty())
            {
               if (rb.getJavaClass().isInterface())
               {
                  ret.addInterfaceFieldValues(tp.getType(), fields);
               }
               else
               {
                  ret.addFieldValue(tp, fields);
               }
            }
         }
         else if (type == ResultType.QUALIFIER)
         {
            ret.addQualifier((Class) rb.getJavaClass());
         }
         else if (type == ResultType.INTERCEPTOR_BINDING)
         {
            ret.addInterceptorBinding((Class) rb.getJavaClass());
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
      else if (rb.getType() == XmlItemType.GENERIC_BEAN)
      {
         GenericBeanXmlItem item = (GenericBeanXmlItem) rb;
         Set<Class> classes = new HashSet<Class>();
         for (ClassXmlItem c : rb.getChildrenOfType(ClassXmlItem.class))
         {
            classes.add(c.getJavaClass());
         }
         ret.addGenericBean(new GenericBeanResult(item.getJavaClass(), classes));
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

      if (ret == null)
      {
         ret = ResultType.BEAN;
      }
      return ret;
   }

   @SuppressWarnings("unchecked")
   BeanResult<?> buildAnnotatedType(ClassXmlItem rb)
   {
      boolean override = !rb.getChildrenOfType(OverrideXmlItem.class).isEmpty();
      boolean extend = !rb.getChildrenOfType(SpecializesXmlItem.class).isEmpty();

      // if it is an extend we want to read the annotations from the underlying
      // class
      BeanResult<?> result = new BeanResult(rb.getJavaClass(), extend);
      NewAnnotatedTypeBuilder<?> type = result.getBuilder();
      // list of constructor arguments
      List<ParameterXmlItem> constList = new ArrayList<ParameterXmlItem>();

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
      List<ParametersXmlItem> constructorParameters = rb.getChildrenOfType(ParametersXmlItem.class);
      if (constructorParameters.size() > 1)
      {
         throw new XmlConfigurationException("A method may only have a single <parameters> element", rb.getDocument(), rb.getLineno());
      }
      else if (!constructorParameters.isEmpty())
      {
         for (ParameterXmlItem item : constructorParameters.get(0).getChildrenOfType(ParameterXmlItem.class))
         {
            constList.add(item);
         }
      }
      for (FieldXmlItem item : rb.getChildrenOfType(FieldXmlItem.class))
      {
         for (AnnotationXmlItem fi : item.getChildrenOfType(AnnotationXmlItem.class))
         {
            Annotation a = createAnnotation(fi);
            type.addToField(item.getField(), a);
         }
         List<TypeXmlItem> types = item.getChildrenOfType(TypeXmlItem.class);
         if (types.size() > 1)
         {
            throw new XmlConfigurationException("Only one <type> element may be present on a field", rb.getDocument(), rb.getLineno());
         }
         if (!types.isEmpty())
         {
            List<ClassXmlItem> overridenTypes = types.get(0).getChildrenOfType(ClassXmlItem.class);
            if (overridenTypes.size() != 1)
            {
               throw new XmlConfigurationException("<type> must have a single child element", rb.getDocument(), rb.getLineno());
            }

            type.overrideFieldType(item.getField(), overridenTypes.get(0).getJavaClass());
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
         List<ParametersXmlItem> parameters = item.getChildrenOfType(ParametersXmlItem.class);
         if (parameters.size() > 1)
         {
            throw new XmlConfigurationException("A method may only have a single <parameters> element", item.getDocument(), item.getLineno());
         }
         else if (!parameters.isEmpty())
         {
            for (ParameterXmlItem fi : parameters.get(0).getChildrenOfType(ParameterXmlItem.class))
            {
               int param = paramCount++;
               for (AnnotationXmlItem pan : fi.getChildrenOfType(AnnotationXmlItem.class))
               {
                  Annotation a = createAnnotation(pan);
                  type.addToMethodParameter(item.getMethod(), param, a);
               }
               List<TypeXmlItem> types = fi.getChildrenOfType(TypeXmlItem.class);
               if (types.size() > 1)
               {
                  throw new XmlConfigurationException("Only one <type> element may be present on a parameter", rb.getDocument(), rb.getLineno());
               }
               if (!types.isEmpty())
               {
                  List<ClassXmlItem> overridenTypes = types.get(0).getChildrenOfType(ClassXmlItem.class);
                  if (overridenTypes.size() != 1)
                  {
                     throw new XmlConfigurationException("<type> must have a single child element", rb.getDocument(), rb.getLineno());
                  }
                  type.overrideMethodParameterType(item.getMethod(), overridenTypes.get(0).getJavaClass(), param);
               }
            }
         }

      }

      if (!constList.isEmpty())
      {
         int paramCount = 0;
         Constructor<?> c = resolveConstructor(rb, constList);
         // we automatically add inject to the constructor
         type.addToConstructor((Constructor) c, new AnnotationLiteral<Inject>()
         {
         });
         for (ParameterXmlItem fi : constList)
         {
            int param = paramCount++;
            for (AnnotationXmlItem pan : fi.getChildrenOfType(AnnotationXmlItem.class))
            {
               Annotation a = createAnnotation(pan);
               type.addToConstructorParameter((Constructor) c, param, a);
            }
            List<TypeXmlItem> types = fi.getChildrenOfType(TypeXmlItem.class);
            if (types.size() > 1)
            {
               throw new XmlConfigurationException("Only one <type> element may be present on a parameter", rb.getDocument(), rb.getLineno());
            }
            if (!types.isEmpty())
            {
               List<ClassXmlItem> overridenTypes = types.get(0).getChildrenOfType(ClassXmlItem.class);
               if (overridenTypes.size() != 1)
               {
                  throw new XmlConfigurationException("<type> must have a single child element", rb.getDocument(), rb.getLineno());
               }

               type.overrideConstructorParameterType(c, overridenTypes.get(0).getJavaClass(), param);
            }
         }
      }
      return result;
   }

   protected static Constructor<?> resolveConstructor(ClassXmlItem bean, List<ParameterXmlItem> constList)
   {
      Class<?>[] params = new Class[constList.size()];
      for (int i = 0; i < constList.size(); ++i)
      {
         params[i] = constList.get(i).getJavaClass();
      }
      Constructor<?> ret = ReflectionUtils.getConstructor(bean.getJavaClass(), params);
      if (ret == null)
      {
         throw new XmlConfigurationException("Could not resolve constructor for " + bean.getJavaClass() + " with arguments " + params, bean.getDocument(), bean.getLineno());
      }
      return ret;
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
      ret.addStereotype((Class) rb.getJavaClass(), values);

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
