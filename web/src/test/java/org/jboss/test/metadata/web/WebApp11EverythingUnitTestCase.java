/*
 * Copyright The JBoss Metadata Authors
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.test.metadata.web;

import java.util.List;

import org.jboss.metadata.merge.javaee.spec.JavaEEVersion;
import org.jboss.metadata.web.spec.AttributeValueMetaData;
import org.jboss.metadata.web.spec.WebMetaData;

/**
 * Test all entries of Jakarta EE 11 web-app.
 *
 * @author Eduardo Martins
 *
 */
public class WebApp11EverythingUnitTestCase extends WebApp6EverythingUnitTestCase {

    public void testEverything() throws Exception {
        WebMetaData webApp = unmarshal();
        assertEverything(webApp, Mode.SPEC, JavaEEVersion.V11);
    }

    protected void assertCookieAttributes(List<AttributeValueMetaData> attributes) {
        assertNotNull("cookie attributes not set", attributes);
        assertEquals(2, attributes.size());
        assertEquals("SameSite", attributes.get(0).getAttributeName());
        assertEquals("None", attributes.get(0).getAttributeValue());
        assertEquals("foo", attributes.get(1).getAttributeName());
        assertEquals("bar", attributes.get(1).getAttributeValue());
    }
}
