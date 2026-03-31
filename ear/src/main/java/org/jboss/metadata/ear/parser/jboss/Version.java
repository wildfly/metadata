/*
 * Copyright The JBoss Metadata Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.jboss.metadata.ear.parser.jboss;

import java.util.HashMap;
import java.util.Map;

/**
 * @author John Bailey
 */
public enum Version {
    UNKNOWN(null, null),
    APP_3_0("https://www.jboss.org/j2ee/dtd/jboss-app_3_0.dtd", "3.0"),
    APP_3_2("https://www.jboss.org/j2ee/dtd/jboss-app_3_2.dtd", "3.2"),
    APP_4_0("https://www.jboss.org/j2ee/dtd/jboss-app_4_0.dtd", "4.0"),
    APP_4_2("https://www.jboss.org/j2ee/dtd/jboss-app_4_2.dtd", "4.2"),
    APP_5_0("https://www.jboss.org/j2ee/dtd/jboss-app_5_0.dtd", "5.0"),
    APP_7_0("https://www.jboss.org/j2ee/schema/jboss-app_7_1.xsd", "7.0"),
    APP_8_0("https://www.jboss.org/j2ee/schema/jboss-app_8_0.xsd", "8.0"),
    APP_9_0("https://www.jboss.org/j2ee/schema/jboss-app_9_0.xsd", "9.0"),
    APP_10_0("https://www.jboss.org/j2ee/schema/jboss-app_10_0.xsd", "10.0"),
    APP_11_0("https://www.jboss.org/j2ee/schema/jboss-app_11_0.xsd", "11.0"),
    APP_11_0_1("https://www.jboss.org/j2ee/schema/jboss-app_11_0_1.xsd", "11.0.1");

    private static final Map<String, Version> bindings = new HashMap<String, Version>();

    private final String location;
    private final String version;

    Version(final String location, final String version) {
        this.location = location;
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    static {
        for (Version version : values()) {
            bindings.put(version.location, version);
        }
    }

    public static Version forLocation(final String location) {
        final Version version = bindings.get(location);
        return version != null ? version : UNKNOWN;
    }
}
