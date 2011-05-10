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
package org.jboss.seam.config.xml.test.method;

import javax.enterprise.util.AnnotationLiteral;

import junit.framework.Assert;
import org.jboss.seam.config.xml.test.AbstractXMLTest;
import org.junit.Test;

public class PrimitiveMethodTest extends AbstractXMLTest {

    @Override
    protected String getXmlFileName() {
        return "primitive-method-beans.xml";
    }

    @Test
    public void methodTest() {
        int x = getReference(int.class, new Qual1Lit());
        Assert.assertTrue(x == 1);
        int[] y = getReference(int[].class, new Qual2Lit());
        for (int i = 0; i < y.length; ++i) {
            Assert.assertTrue(y[i] == i + 2);
        }

    }

    class Qual1Lit extends AnnotationLiteral<Qualifier1> {
    }

    ;

    class Qual2Lit extends AnnotationLiteral<Qualifier2> {
    }

    ;

}
