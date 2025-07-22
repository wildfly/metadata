/*
 * Copyright The JBoss Metadata Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.jboss.metadata.parser.jsp;

import java.util.HashMap;

public class Location {
    private static final HashMap<String, Version> bindings = new HashMap<String, Version>();

    static {
        bindings.put("http://java.sun.com/j2ee/dtds/web-jsptaglibrary_1_1.dtd", Version.TLD_1_1);
        bindings.put("http://java.sun.com/dtd/web-jsptaglibrary_1_2.dtd", Version.TLD_1_2);
        bindings.put("http://java.sun.com/xml/ns/j2ee/web-jsptaglibrary_2_0.xsd", Version.TLD_2_0);
        bindings.put("http://java.sun.com/xml/ns/javaee/web-jsptaglibrary_2_1.xsd", Version.TLD_2_1);
        bindings.put("http://xmlns.jcp.org/xml/ns/javaee/web-jsptaglibrary_2_1.xsd", Version.TLD_2_1);
        bindings.put("https://jakarta.ee/xml/ns/jakartaee/web-jsptaglibrary_3_0.xsd", Version.TLD_3_0);
        bindings.put("https://jakarta.ee/xml/ns/jakartaee/web-jsptaglibrary_3_1.xsd", Version.TLD_3_1);
        bindings.put("https://jakarta.ee/xml/ns/jakartaee/web-jsptaglibrary_4_0.xsd", Version.TLD_4_0);
    }

    public static Version getVersion(String location) {
        return bindings.get(location);
    }
}
