/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.test.generic;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface HighGenericQualifier
{
  
}
