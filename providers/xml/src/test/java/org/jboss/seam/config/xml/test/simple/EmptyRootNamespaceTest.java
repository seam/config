package org.jboss.seam.config.xml.test.simple;


/**
 * This test verifies that a no-namespace root element does not break the deployment.
 *
 * @author <a href="http://community.jboss.org/people/jharting">Jozef Hartinger</a>
 * @see https://issues.jboss.org/browse/SEAMXML-45
 */
public class EmptyRootNamespaceTest extends SimpleBeanTest {

    @Override
    protected String getXmlFileName() {
        return "empty-root-namespace-beans.xml";
    }
}
