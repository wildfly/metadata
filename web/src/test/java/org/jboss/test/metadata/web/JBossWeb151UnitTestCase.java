/*
 * Copyright The JBoss Metadata Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.jboss.test.metadata.web;


import javax.xml.stream.XMLStreamException;

import org.jboss.metadata.parser.jbossweb.JBossWebMetaDataParser;
import org.jboss.metadata.property.PropertyReplacers;
import org.jboss.metadata.web.jboss.JBossWebMetaData;
import org.jboss.test.metadata.javaee.AbstractJavaEEEverythingTest;
import org.junit.Assert;

public class JBossWeb151UnitTestCase extends AbstractJavaEEEverythingTest {

    public void testReplicationConfig() throws Exception {
        try {
            JBossWebMetaDataParser.parse(getReader(), PropertyReplacers.noop());
            Assert.fail("<replication-config/> is no longer valid");
        } catch (XMLStreamException e) {
            // Expected
        }
    }

    @SuppressWarnings("deprecation")
    public void testClustering() throws Exception {
        JBossWebMetaData metaData = JBossWebMetaDataParser.parse(getReader(), PropertyReplacers.noop());
        Assert.assertNull(metaData.getReplicationConfig());
    }
}
