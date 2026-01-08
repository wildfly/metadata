/*
 * Copyright The JBoss Metadata Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.jboss.metadata.parser.servlet;

import java.util.EnumSet;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Version implements org.jboss.metadata.parser.util.Version<Version> {

    SERVLET_2_2(2, 2, "http://java.sun.com/j2ee/dtds/web-app_%d_%d.dtd"),
    SERVLET_2_3(2, 3, "http://java.sun.com/dtd/web-app_%d_%d.dtd"),
    SERVLET_2_4(2, 4, "http://java.sun.com/xml/ns/j2ee/web-app_%d_%d.xsd"),
    SERVLET_2_5(2, 5, "http://java.sun.com/xml/ns/javaee/web-app_%d_%d.xsd"),
    SERVLET_3_0(3, 0, "http://java.sun.com/xml/ns/javaee/web-app_%d_%d.xsd"),
    SERVLET_3_1(3, 1, "http://xmlns.jcp.org/xml/ns/javaee/web-app_%d_%d.xsd"),
    SERVLET_4_0(4, 0, "http://xmlns.jcp.org/xml/ns/javaee/web-app_%d_%d.xsd"),
    SERVLET_5_0(5, 0),
    SERVLET_6_0(6, 0),
    SERVLET_6_1(6, 1),
    ;
    public static final Version LATEST = Version.SERVLET_6_1;
    private static final Map<String, Version> VERSIONS = EnumSet.allOf(Version.class).stream().collect(Collectors.toMap(Version::toString, Function.identity()));
    private static final Map<String, Version> SYSTEM_IDS = EnumSet.allOf(Version.class).stream().collect(Collectors.toMap(Version::getSystemId, Function.identity()));

    private static final Map<String, Version> PUBLIC_IDS = EnumSet.of(Version.SERVLET_2_2, Version.SERVLET_2_3).stream()
            .collect(Collectors.toMap(version -> String.format("-//Sun Microsystems, Inc.//DTD Web Application %d.%d//EN", version.major(), version.minor()), Function.identity()));

    private final int major;
    private final int minor;
    private final String systemId;

    Version(int major, int minor) {
        this(major, minor, "https://jakarta.ee/xml/ns/jakartaee/web-app_%d_%d.xsd");
    }

    Version(int major, int minor, String systemIdFormat) {
        this.major = major;
        this.minor = minor;
        this.systemId = String.format(systemIdFormat, major, minor);
    }

    @Override
    public int major() {
        return this.major;
    }

    @Override
    public int minor() {
        return this.minor;
    }

    @Override
    public String toString() {
        return this.major + "." + this.minor;
    }

    public String getSystemId() {
        return this.systemId;
    }

    /**
     * @deprecated Use {@link #toString()} instead.
     */
    @Deprecated(forRemoval = true)
    public String versionString() {
        return this.toString();
    }

    public static Version fromString(String version) {
        return VERSIONS.get(version);
    }

    public static Version fromSystemID(String systemID) {
        return SYSTEM_IDS.get(systemID);
    }

    public static Version fromPublicID(String publicID) {
        return PUBLIC_IDS.get(publicID);
    }
}
