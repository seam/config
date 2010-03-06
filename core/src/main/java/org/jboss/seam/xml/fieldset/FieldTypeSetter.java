/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.fieldset;

import java.lang.reflect.Field;

/**
 * Interface the can set primitive types
 * 
 * @author Stuart Douglas <stuart@baileyroberts.com.au>
 * 
 */
public interface FieldTypeSetter
{

   public void setField(String value, Field field);

}
