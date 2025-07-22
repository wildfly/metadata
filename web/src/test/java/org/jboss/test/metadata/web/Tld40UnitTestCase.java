/*
 * Copyright The JBoss Metadata Authors
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.test.metadata.web;

import org.jboss.metadata.parser.jsp.TldMetaDataParser;
import org.jboss.test.metadata.javaee.AbstractJavaEEEverythingTest;

/**
 * Tests of 4. taglib elements
 *
 * @author Brian Stansberry
 */
public class Tld40UnitTestCase extends AbstractJavaEEEverythingTest {
    public void testEverything() throws Exception {
        TldMetaDataParser.parse(getReader());
    }
}
