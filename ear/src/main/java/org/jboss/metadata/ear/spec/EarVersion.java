/*
 * Copyright The JBoss Metadata Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.jboss.metadata.ear.spec;

import java.util.HashMap;
import java.util.Map;

import org.jboss.metadata.merge.javaee.spec.JavaEEVersion;

/**
 * @author John Bailey
 */
public enum EarVersion {

    UNKNOWN(null, null, JavaEEVersion.UNKNOWN),
    APP_1_3("http://java.sun.com/dtd/application_1_3.dtd", "1.3", JavaEEVersion.UNKNOWN),
    APP_1_4("http://java.sun.com/xml/ns/j2ee/application_1_4.xsd", "1.4", JavaEEVersion.V1_4),
    APP_5_0("http://java.sun.com/xml/ns/javaee/application_5.xsd", "5.0", JavaEEVersion.V5),
    APP_6_0("http://java.sun.com/xml/ns/javaee/application_6.xsd", "6.0", JavaEEVersion.V6),
    APP_7_0("http://xmlns.jcp.org/xml/ns/javaee/application_7.xsd", "7.0", JavaEEVersion.V7),
    APP_8_0("http://xmlns.jcp.org/xml/ns/javaee/application_8.xsd", "8.0", JavaEEVersion.V8),
    APP_9_0("https://jakarta.ee/xml/ns/jakartaee/application_9.xsd", "9.0", JavaEEVersion.V9),
    APP_10_0("https://jakarta.ee/xml/ns/jakartaee/application_10.xsd", "10.0", JavaEEVersion.V10),
    APP_11_0("https://jakarta.ee/xml/ns/jakartaee/application_11.xsd", "11.0", JavaEEVersion.V11);

    private static final Map<String, EarVersion> bindings = new HashMap<>();

    private final String location;
    private final String version;
    private final JavaEEVersion javaEEVersion;

    EarVersion(final String location, final String version, final JavaEEVersion javaEEVersion) {
        this.location = location;
        this.version = version;
        this.javaEEVersion = javaEEVersion;
    }

    public String getVersion() {
        return version;
    }

    public JavaEEVersion getJavaEEVersion() {
        return javaEEVersion;
    }

    static {
        for (EarVersion version : values()) {
            bindings.put(version.location, version);
        }
    }

    public static EarVersion forLocation(final String location) {
        final EarVersion version = bindings.get(location);
        return version != null ? version : UNKNOWN;
    }

}
