package org.jboss.seam.xml.test.xmlconfigured;

import java.util.Set;

import javax.enterprise.inject.spi.Bean;

import org.jboss.seam.xml.test.AbstractXMLTest;
import org.testng.annotations.Test;

public class XmlConfiguredTest extends AbstractXMLTest
{

	   @Override
	   protected String getXmlFileName()
	   {
	      return "xmlconfigured-beans.xml";
	   }

	   @Test
	   public void testXmlConfiguredBeanNotInstalled()
	   {

	      Set<Bean<?>> beans = manager.getBeans(XmlConfiguredNotInstalledBean.class);
	      assert beans.isEmpty();
	   }
	   
	   @Test(enabled = true)
	   public void testXmlConfiguredBean()
	   {

	      Set<Bean<?>> beans = manager.getBeans(XmlConfiguredBean.class);
	      assert beans.size() == 1;
	   }
}
