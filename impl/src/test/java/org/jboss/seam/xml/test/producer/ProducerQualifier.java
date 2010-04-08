package org.jboss.seam.xml.test.producer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface ProducerQualifier
{
   int value();
}
