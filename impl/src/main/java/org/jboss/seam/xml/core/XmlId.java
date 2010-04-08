/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation that allows tracing of annotated types through the startup process
 * 
 * TODO: This approach currently breaks replication as the id's are non
 * deterministic
 * 
 * @author stuart
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface XmlId
{
   int value();
}
