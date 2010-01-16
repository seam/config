/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation that allows tracing of annotated types through the startup proces
 * 
 * @author stuart
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface XmlId
{
   int value();
}
