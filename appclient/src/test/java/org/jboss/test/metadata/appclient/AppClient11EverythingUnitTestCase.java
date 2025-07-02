/*
 * Copyright The JBoss Metadata Authors
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.test.metadata.appclient;

import org.jboss.metadata.appclient.parser.spec.ApplicationClientMetaDataParser;
import org.jboss.metadata.appclient.spec.AppClientEnvironmentRefsGroupMetaData;
import org.jboss.metadata.appclient.spec.ApplicationClientMetaData;
import org.jboss.metadata.merge.javaee.spec.JavaEEVersion;
import org.jboss.test.metadata.javaee.AbstractJavaEEEverythingTest;

/**
 * App Client 11 everything test.
 *
 * @author Eduardo Martins
 *
 */
public class AppClient11EverythingUnitTestCase extends AbstractJavaEEEverythingTest {

    protected ApplicationClientMetaData unmarshal() throws Exception {
        return ApplicationClientMetaDataParser.INSTANCE.parse(getReader());
    }

    public void testEverything() throws Exception {
        // enableTrace("org.jboss.xb");
        ApplicationClientMetaData result = unmarshal();
        String prefix = "app";
        assertEquals("11", result.getVersion());
        assertEquals(prefix, result.getId());
        assertTrue(result.isMetadataComplete());
        assertEquals(prefix + "ModuleName", result.getModuleName());
        assertDescriptionGroup(prefix, result.getDescriptionGroup());
        assertEnvironment(prefix, result.getEnvironmentRefsGroupMetaData());
    }

    protected void assertEnvironment(String prefix, AppClientEnvironmentRefsGroupMetaData env) {
        assertNotNull(env);
        final Mode mode = Mode.SPEC;
        final JavaEEVersion javaEEVersion = JavaEEVersion.V11;
        assertRemoteEnvironment(prefix, env, true, Descriptor.APPLICATION_CLIENT, mode, javaEEVersion);
        assertMessageDestinations(2,env.getMessageDestinations(),  mode, javaEEVersion);
    }

}
