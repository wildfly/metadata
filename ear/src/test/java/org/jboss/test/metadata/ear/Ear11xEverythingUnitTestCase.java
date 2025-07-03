/*
 * Copyright The JBoss Metadata Authors
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.test.metadata.ear;

import org.jboss.metadata.ear.parser.spec.EarMetaDataParser;
import org.jboss.metadata.ear.spec.ConnectorModuleMetaData;
import org.jboss.metadata.ear.spec.EarEnvironmentRefsGroupMetaData;
import org.jboss.metadata.ear.spec.EarMetaData;
import org.jboss.metadata.ear.spec.EarVersion;
import org.jboss.metadata.ear.spec.EjbModuleMetaData;
import org.jboss.metadata.ear.spec.JavaModuleMetaData;
import org.jboss.metadata.ear.spec.ModuleMetaData;
import org.jboss.metadata.ear.spec.ModulesMetaData;
import org.jboss.metadata.ear.spec.WebModuleMetaData;
import org.jboss.test.metadata.javaee.AbstractJavaEEEverythingTest;

/**
 * Ear10x tests.
 *
 * @author Brian Stansberry
 */
public class Ear11xEverythingUnitTestCase extends AbstractJavaEEEverythingTest {

    protected EarMetaData unmarshal() throws Exception {
        final EarMetaData earMetaData = EarMetaDataParser.INSTANCE.parse(getReader());
        assertTrue(earMetaData instanceof  EarMetaData);
        return EarMetaData.class.cast(earMetaData);
    }

    protected EarVersion getExpectedEarVersion() {
        return EarVersion.APP_11_0;
    }

    public void testEverything() throws Exception {
        EarMetaData result = unmarshal();

        assertEquals("application-test-everything", result.getId());
        assertEquals(getExpectedEarVersion(), result.getEarVersion());
        assertEquals("ApplicationName", result.getApplicationName());

        String prefix = "app";
        final Mode mode = Mode.SPEC;

        assertDescriptionGroup(prefix, result.getDescriptionGroup());
        assertSecurityRoles(2,result.getSecurityRoles(), mode);
        assertLibraryDirectory(result);
        assertModules(result);
        assertTrue(result.getInitializeInOrder());
        assertEarEnvironment(prefix,result.getEarEnvironmentRefsGroup(), result.getEarVersion(), mode);
    }

    protected void assertEarEnvironment(String prefix, EarEnvironmentRefsGroupMetaData earEnvironmentRefsGroup, EarVersion earVersion, Mode mode) {
        assertNotNull(earEnvironmentRefsGroup);
        final boolean full = true;
        assertEnvironment(prefix, earEnvironmentRefsGroup, full, Descriptor.APPLICATION, mode, earVersion.getJavaEEVersion());
        assertMessageDestinations(2,earEnvironmentRefsGroup.getMessageDestinations(),mode, earVersion.getJavaEEVersion());
    }

    protected void assertLibraryDirectory(EarMetaData ear) {
        assertEquals("lib0", ear.getLibraryDirectory());
    }

    protected void assertModules(EarMetaData ear) {
        ModulesMetaData modules = ear.getModules();
        assertEquals(6, modules.size());
        ModuleMetaData connector = modules.get(0);
        assertEquals("connector0", connector.getId());
        assertEquals("META-INF/alt-ra.xml", connector.getAlternativeDD());
        ConnectorModuleMetaData connectorMD = (ConnectorModuleMetaData) connector.getValue();
        assertEquals("rar0.rar", connectorMD.getConnector());
        ModuleMetaData java = modules.get(1);
        assertEquals("java0", java.getId());
        assertEquals("META-INF/alt-application-client.xml", java.getAlternativeDD());
        JavaModuleMetaData javaMD = (JavaModuleMetaData) java.getValue();
        assertEquals("client0.jar", javaMD.getClientJar());
        ModuleMetaData ejb0 = modules.get(2);
        assertEquals("ejb0", ejb0.getId());
        assertEquals("META-INF/alt-ejb-jar.xml", ejb0.getAlternativeDD());
        EjbModuleMetaData ejb0MD = (EjbModuleMetaData) ejb0.getValue();
        assertEquals("ejb-jar0.jar", ejb0MD.getEjbJar());
        ModuleMetaData ejb1 = modules.get(3);
        assertEquals("ejb1", ejb1.getId());
        assertEquals("META-INF/alt-ejb-jar.xml", ejb1.getAlternativeDD());
        EjbModuleMetaData ejb1MD = (EjbModuleMetaData) ejb1.getValue();
        assertEquals("ejb-jar1.jar", ejb1MD.getEjbJar());
        ModuleMetaData web0 = modules.get(4);
        assertEquals("web0", web0.getId());
        assertEquals("WEB-INF/alt-web.xml", web0.getAlternativeDD());
        WebModuleMetaData web0MD = (WebModuleMetaData) web0.getValue();
        assertEquals("/web0", web0MD.getContextRoot());
        assertEquals("web-app0.war", web0MD.getWebURI());
        ModuleMetaData web1 = modules.get(5);
        assertEquals("web1", web1.getId());
        assertEquals("WEB-INF/alt-web.xml", web1.getAlternativeDD());
        WebModuleMetaData web1MD = (WebModuleMetaData) web1.getValue();
        assertEquals("/web1", web1MD.getContextRoot());
        assertEquals("web-app1.war", web1MD.getWebURI());
    }
}
