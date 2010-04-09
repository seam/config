package org.jboss.seam.xml.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Signifies that a bean is designed to be configured through XML, and
 * should therefore not be installed by default. 
 * @author Stuart Douglas
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface XmlConfigured {

}
