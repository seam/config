/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.test.stereotype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Stereotype1
{

}
