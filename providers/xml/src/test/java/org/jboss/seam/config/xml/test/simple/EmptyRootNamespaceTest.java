package org.jboss.seam.config.xml.test.simple;


/**
 * This test verifies that a no-namespace root element does not break the deployment.
 * @see https://issues.jboss.org/browse/SEAMXML-45
 * 
 * @author <a href="http://community.jboss.org/people/jharting">Jozef Hartinger</a>
 *
 */
public class EmptyRootNamespaceTest extends SimpleBeanTest {

    @Override
    protected String getXmlFileName() {
        return "empty-root-namespace-beans.xml";
    }
}
