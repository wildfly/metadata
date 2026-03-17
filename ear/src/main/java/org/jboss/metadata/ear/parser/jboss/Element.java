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
public enum Element {
    // must be first
    UNKNOWN(null),
    CONTEXT_ROOT("context-root"),
    DISTINCT_NAME("distinct-name"),
    HAR("har"),
    JMX_NAME("jmx-name"),
    LIBRARY_DIRECTORY("library-directory"),
    LIMIT_APPCLIENT_MODULES("limit-appclient-modules"),
    LOADER_REPOSITORY("loader-repository"),
    LOADER_REPOSITORY_CONFIG("loader-repository-config"),
    MODULE("module"),
    MODULE_ORDER("module-order"),
    PRINCIPAL_NAME("principal-name"),
    ROLE_NAME("role-name"),
    SECURITY_DOMAIN("security-domain"),
    SECURITY_ROLE("security-role"),
    SERVICE("service"),
    UNAUTHENTICATED_PRINCIPAL("unauthenticated-principal"),
    WEB("web"),
    WEB_URI("web-uri");

    private final String name;

    Element(final String name) {
        this.name = name;
    }

    /**
     * Get the local name of this element.
     *
     * @return the local name
     */
    public String getLocalName() {
        return name;
    }

    private static final Map<String, Element> MAP;

    static {
        final Map<String, Element> map = new HashMap<String, Element>();
        for (Element element : values()) {
            final String name = element.getLocalName();
            if (name != null) map.put(name, element);
        }
        MAP = map;
    }

    public static Element forName(String localName) {
        final Element element = MAP.get(localName);
        return element == null ? UNKNOWN : element;
    }
}
