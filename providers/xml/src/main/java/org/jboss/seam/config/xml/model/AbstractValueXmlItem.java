package org.jboss.seam.config.xml.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.spi.BeanManager;

import org.jboss.seam.config.xml.core.BeanResult;
import org.jboss.seam.config.xml.fieldset.ConstantFieldValue;
import org.jboss.seam.config.xml.fieldset.ELFieldValue;
import org.jboss.seam.config.xml.fieldset.FieldValue;
import org.jboss.seam.config.xml.fieldset.InlineBeanFieldValue;
import org.jboss.seam.config.xml.fieldset.InlineBeanIdCreator;
import org.jboss.seam.config.xml.fieldset.InlineBeanQualifier;
import org.jboss.seam.config.xml.util.TypeOccuranceInformation;
import org.jboss.seam.config.xml.util.XmlConfigurationException;

public abstract class AbstractValueXmlItem extends AbstractXmlItem
{

   private int syntheticQualifierId;
   private BeanResult<?> inlineBean;

   public AbstractValueXmlItem(XmlItemType type, XmlItem parent, Class<?> javaClass, String innerText, Map<String, String> attributes, String document, int lineno)
   {
      super(type, parent, javaClass, innerText, attributes, document, lineno);
   }

   public Set<TypeOccuranceInformation> getAllowedItem()
   {
      return Collections.singleton(TypeOccuranceInformation.of(XmlItemType.CLASS, null, 1));
   }

   public BeanResult<?> getBeanResult(BeanManager manager)
   {
      List<ClassXmlItem> inlineBeans = getChildrenOfType(ClassXmlItem.class);
      if (!inlineBeans.isEmpty())
      {
         ClassXmlItem inline = inlineBeans.get(0);
         for (AnnotationXmlItem i : inline.getChildrenOfType(AnnotationXmlItem.class))
         {
            Class annotation = (Class) i.getJavaClass();
            if (manager.isQualifier(annotation))
            {
               throw new XmlConfigurationException("Cannot define qualifiers on inline beans, Qualifier: " + annotation.getName(), i.getDocument(), i.getLineno());
            }
            else if (manager.isScope(annotation) && annotation != Dependent.class)
            {
               throw new XmlConfigurationException("Inline beans must have @Dependent scope, Scope: " + annotation.getName(), i.getDocument(), i.getLineno());
            }
         }
         syntheticQualifierId = InlineBeanIdCreator.getId();
         AnnotationXmlItem syntheticQualifier = new AnnotationXmlItem(this, InlineBeanQualifier.class, "" + syntheticQualifierId, Collections.EMPTY_MAP, getDocument(), getLineno());
         inline.addChild(syntheticQualifier);
         inlineBean = inline.createBeanResult(manager);
         return inlineBean;
      }
      inlineBean = null;
      return null;
   }

   public int getSyntheticQualifierId()
   {
      return syntheticQualifierId;
   }

   public FieldValue getValue()
   {
      if (inlineBean == null)
      {
         if (innerText.matches("^#\\{.*\\}$"))
         {
            return new ELFieldValue(innerText);
         }
         return new ConstantFieldValue(innerText);
      }
      else
      {
         return new InlineBeanFieldValue(syntheticQualifierId);
      }
   }

}