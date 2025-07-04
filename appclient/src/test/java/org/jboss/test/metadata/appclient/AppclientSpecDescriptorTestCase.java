/*
 * Copyright The JBoss Metadata Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.jboss.test.metadata.appclient;

import java.util.Arrays;
import java.util.List;

import org.jboss.test.metadata.common.SpecDescriptorTestCase;
import org.junit.runners.Parameterized.Parameters;

/**
 * Guard existence of spec descriptor in appclient module
 *
 * @author Chao Wang
 */
public class AppclientSpecDescriptorTestCase extends SpecDescriptorTestCase {

    @Parameters
    public static List<Object[]> parameters() {
        // The spec descriptor should be guarded in schema
        return Arrays.asList(new Object[][]{
                {"schema/application-client_6.xsd"},
                {"schema/application-client_7.xsd"},
                {"schema/application-client_8.xsd"},
                {"schema/application-client_9.xsd"},
                {"schema/application-client_10.xsd"},
                {"schema/application-client_11.xsd"},
        });
    }

    public AppclientSpecDescriptorTestCase(String xsd) {
        super(xsd);
        // TODO Auto-generated constructor stub
    }
}