package org.jboss.seam.xml.examples.princess;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Room
{
   String value();
}
