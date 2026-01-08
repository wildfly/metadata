/*
 * Copyright The JBoss Metadata Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.jboss.metadata.parser.jbossweb;

import java.util.EnumSet;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Version implements org.jboss.metadata.parser.util.Version<Version> {
    JBOSS_WEB_3_0(org.jboss.metadata.parser.servlet.Version.SERVLET_3_0, 3, 0, "http://www.jboss.org/j2ee/dtd/jboss-web_%d_%d.dtd"),
    JBOSS_WEB_3_2(org.jboss.metadata.parser.servlet.Version.SERVLET_3_0, 3, 2, "http://www.jboss.org/j2ee/dtd/jboss-web_%d_%d.dtd"),
    JBOSS_WEB_4_0(org.jboss.metadata.parser.servlet.Version.SERVLET_3_0, 4, 0, "http://www.jboss.org/j2ee/dtd/jboss-web_%d_%d.dtd"),
    JBOSS_WEB_4_2(org.jboss.metadata.parser.servlet.Version.SERVLET_3_0, 4, 2, "http://www.jboss.org/j2ee/dtd/jboss-web_%d_%d.dtd"),
    JBOSS_WEB_5_0(org.jboss.metadata.parser.servlet.Version.SERVLET_3_0, 5, 0, "http://www.jboss.org/j2ee/dtd/jboss-web_%d_%d.dtd"),
    JBOSS_WEB_5_1(org.jboss.metadata.parser.servlet.Version.SERVLET_3_0, 5, 1),
    JBOSS_WEB_6_0(org.jboss.metadata.parser.servlet.Version.SERVLET_3_0, 6, 0),
    JBOSS_WEB_7_0(org.jboss.metadata.parser.servlet.Version.SERVLET_3_0, 7, 0),
    JBOSS_WEB_7_1(org.jboss.metadata.parser.servlet.Version.SERVLET_3_0, 7, 1),
    JBOSS_WEB_7_2(org.jboss.metadata.parser.servlet.Version.SERVLET_3_0, 7, 2),
    JBOSS_WEB_8_0(org.jboss.metadata.parser.servlet.Version.SERVLET_3_1, 8, 0),
    JBOSS_WEB_10_0(org.jboss.metadata.parser.servlet.Version.SERVLET_3_1, 10, 0),
    JBOSS_WEB_11_0(org.jboss.metadata.parser.servlet.Version.SERVLET_3_1, 11, 0),
    JBOSS_WEB_12_0(org.jboss.metadata.parser.servlet.Version.SERVLET_3_1, 12, 0),
    JBOSS_WEB_12_1(org.jboss.metadata.parser.servlet.Version.SERVLET_3_1, 12, 1),
    JBOSS_WEB_13_0(org.jboss.metadata.parser.servlet.Version.SERVLET_4_0, 13, 0),
    JBOSS_WEB_14_0(org.jboss.metadata.parser.servlet.Version.SERVLET_4_0, 14, 0),
    JBOSS_WEB_14_1(org.jboss.metadata.parser.servlet.Version.SERVLET_4_0, 14, 1),
    JBOSS_WEB_15_0(org.jboss.metadata.parser.servlet.Version.SERVLET_6_0, 15, 0),
    JBOSS_WEB_15_1(org.jboss.metadata.parser.servlet.Version.SERVLET_6_1, 15, 1),
    ;
    // The corresponding servlet version
    private final org.jboss.metadata.parser.servlet.Version servletVersion;
    private final int major;
    private final int minor;
    private final String location;

    Version(org.jboss.metadata.parser.servlet.Version version, int major, int minor) {
        this(version, major, minor, "https://www.jboss.org/j2ee/schema/jboss-web_%d_%d.xsd");
    }

    Version(org.jboss.metadata.parser.servlet.Version servletVersion, int major, int minor, String locationPattern) {
        this.servletVersion = servletVersion;
        this.major = major;
        this.minor = minor;
        this.location = String.format(locationPattern, major, minor);
    }

    public org.jboss.metadata.parser.servlet.Version getServletVersion() {
        return this.servletVersion;
    }

    @Override
    public int major() {
        return this.major;
    }

    @Override
    public int minor() {
        return this.minor;
    }

    String getLocation() {
        return this.location;
    }

    /**
     * @deprecated Use {@link #toString()} instead.
     */
    @Deprecated(forRemoval = true)
    public String versionString() {
        return this.toString();
    }

    @Override
    public String toString() {
        return this.major + "." + this.minor;
    }

    private static final Map<String, Version> VERSIONS = EnumSet.allOf(Version.class).stream().collect(Collectors.toMap(Version::toString, Function.identity()));
    private static final Map<String, Version> LOCATIONS = EnumSet.allOf(Version.class).stream().collect(Collectors.toMap(Version::getLocation, Function.identity()));

    public static Version fromLocation(String location) {
        return LOCATIONS.get(location);
    }

    public static Version fromString(String version) {
        return VERSIONS.get(version);
    }
}
