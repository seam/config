/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.seam.config.xml.test.fieldset;

import java.util.Map.Entry;

import junit.framework.Assert;
import org.jboss.seam.config.xml.test.AbstractXMLTest;
import org.junit.Test;

public class ELFieldValueBeanTest extends AbstractXMLTest {

    @Override
    protected String getXmlFileName() {
        return "el-set-field-value-beans.xml";
    }

    @Test
    public void mapSetFieldValue() {
        ELValueBean bean = getReference(ELValueBean.class);
        Assert.assertTrue(bean.array.length == 1);
        Assert.assertEquals(bean.array[0], ELValueProducer.EL_VALUE_STRING);
        Assert.assertTrue(bean.list.size() == 1);
        Assert.assertEquals(bean.list.get(0), ELValueProducer.EL_VALUE_STRING);
        Assert.assertTrue(bean.map.size() == 1);
        Entry<String, String> entry = bean.map.entrySet().iterator().next();
        Assert.assertEquals(entry.getKey(), ELValueProducer.EL_VALUE_STRING);
        Assert.assertEquals(entry.getValue(), ELValueProducer.EL_VALUE_STRING);
    }
}
