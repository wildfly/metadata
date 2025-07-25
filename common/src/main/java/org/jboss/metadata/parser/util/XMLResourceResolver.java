/*
 * Copyright The JBoss Metadata Authors
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.metadata.parser.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.stream.XMLResolver;
import javax.xml.stream.XMLStreamException;

import org.jboss.logging.Logger;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Local entity resolver to handle standard J2EE DTDs and Schemas as well as JBoss
 * specific DTDs.
 *
 * @author Scott.Stark@jboss.org
 * @author Thomas.Diesler@jboss.org
 * @author Dimitris.Andreadis@jboss.org
 * @author Eduardo Martins
 */
public class XMLResourceResolver implements XMLResolver, EntityResolver, LSResourceResolver {
    private static final Logger log = Logger.getLogger(XMLResourceResolver.class);

    /**
     * A class wide Map<String, String> of publicId/systemId to dtd/xsd file
     */
    private static Map entities = new ConcurrentHashMap();
    /**
     * A class flag indicating whether an attempt to resolve a systemID as a
     * non-file URL should produce a warning rather than a trace level log msg.
     */
    private static boolean warnOnNonFileURLs;

    /**
     * A local entities map that overrides the class level entities
     */
    private Map localEntities;

    private ThreadLocal<Boolean> entityResolved = new ThreadLocal<Boolean>();

    static {
        AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                warnOnNonFileURLs = new Boolean(System.getProperty("org.jboss.resolver.warning", "false")).booleanValue();
                return null;
            }
        });

        // xml
        registerEntity("-//W3C//DTD/XMLSCHEMA 200102//EN", "XMLSchema.dtd");
        registerEntity("http://www.w3.org/2001/XMLSchema.dtd", "XMLSchema.dtd");
        registerEntity("datatypes", "datatypes.dtd"); // This dtd doesn't have a publicId - see XMLSchema.dtd
        registerEntity("http://www.w3.org/XML/1998/namespace", "xml.xsd");
        registerEntity("http://www.w3.org/2001/xml.xsd", "xml.xsd");
        registerEntity("http://www.w3.org/2005/05/xmlmime", "xml-media-types.xsd");

        // app
        registerEntity("-//Sun Microsystems, Inc.//DTD J2EE Application 1.2//EN", "application_1_2.dtd");
        registerEntity("-//Sun Microsystems, Inc.//DTD J2EE Application 1.3//EN", "application_1_3.dtd");
        registerEntity("-//Sun Microsystems, Inc.//DTD J2EE Application Client 1.3//EN", "application-client_1_3.dtd");
        registerEntity("http://java.sun.com/xml/ns/j2ee/application_1_4.xsd", "application_1_4.xsd");
        registerEntity("http://java.sun.com/xml/ns/javaee/application_5.xsd", "application_5.xsd");
        registerEntity("http://java.sun.com/xml/ns/javaee/application_6.xsd", "application_6.xsd");
        registerEntity("http://xmlns.jcp.org/xml/ns/javaee/application_7.xsd", "application_7.xsd");
        registerEntity("http://xmlns.jcp.org/xml/ns/javaee/application_8.xsd", "application_8.xsd");
        registerEntity("https://jakarta.ee/xml/ns/jakartaee/application_9.xsd", "application_9.xsd");
        registerEntity("https://jakarta.ee/xml/ns/jakartaee/application_10.xsd", "application_10.xsd");
        registerEntity("https://jakarta.ee/xml/ns/jakartaee/application_11.xsd", "application_11.xsd");

        // jboss-app
        registerEntity("-//JBoss//DTD J2EE Application 1.3//EN", "jboss-app_3_0.dtd");
        registerEntity("-//JBoss//DTD J2EE Application 1.3V2//EN", "jboss-app_3_2.dtd");
        registerEntity("-//JBoss//DTD J2EE Application 1.4//EN", "jboss-app_4_0.dtd");
        registerEntity("-//JBoss//DTD J2EE Application 4.2//EN", "jboss-app_4_2.dtd");
        registerEntity("-//JBoss//DTD Java EE Application 5.0//EN", "jboss-app_5_0.dtd");

        // app client
        registerEntity("http://java.sun.com/xml/ns/j2ee/application-client_1_4.xsd", "application-client_1_4.xsd");
        registerEntity("http://java.sun.com/xml/ns/javaee/application-client_5.xsd", "application-client_5.xsd");
        registerEntity("http://java.sun.com/xml/ns/javaee/application-client_6.xsd", "application-client_6.xsd");
        registerEntity("http://xmlns.jcp.org/xml/ns/javaee/application-client_7.xsd", "application-client_7.xsd");
        registerEntity("http://xmlns.jcp.org/xml/ns/javaee/application-client_8.xsd", "application-client_8.xsd");
        registerEntity("https://jakarta.ee/xml/ns/jakartaee/application-client_9.xsd", "application-client_9.xsd");
        registerEntity("https://jakarta.ee/xml/ns/jakartaee/application-client_10.xsd", "application-client_10.xsd");
        registerEntity("https://jakarta.ee/xml/ns/jakartaee/application-client_11.xsd", "application-client_11.xsd");

        // jboss-client
        registerEntity("-//JBoss//DTD Application Client 3.2//EN", "jboss-client_3_2.dtd");
        registerEntity("-//JBoss//DTD Application Client 4.0//EN", "jboss-client_4_0.dtd");
        registerEntity("-//JBoss//DTD Application Client 4.2//EN", "jboss-client_4_2.dtd");
        registerEntity("-//JBoss//DTD Application Client 5.0//EN", "jboss-client_5_0.dtd");
        registerEntity("https://www.jboss.org/j2ee/schema/jboss-client_5_1.xsd", "jboss-client_5_1.xsd");

        // beans
        registerEntity("http://java.sun.com/xml/ns/javaee/beans_1_0.xsd", "beans_1_0.xsd");
        registerEntity("http://xmlns.jcp.org/xml/ns/javaee/beans_1_1.xsd", "beans_1_1.xsd");

        // connector
        registerEntity("-//Sun Microsystems, Inc.//DTD Connector 1.0//EN", "connector_1_0.dtd");
        registerEntity("http://java.sun.com/xml/ns/j2ee/connector_1_5.xsd", "connector_1_5.xsd");
        registerEntity("http://java.sun.com/xml/ns/javaee/connector_1_6.xsd", "connector_1_6.xsd");
        registerEntity("http://xmlns.jcp.org/xml/ns/javaee/connector_1_7.xsd", "connector_1_7.xsd");
        // jboss-ds
        registerEntity("-//JBoss//DTD JBOSS JCA Config 1.0//EN", "jboss-ds_1_0.dtd");
        registerEntity("-//JBoss//DTD JBOSS JCA Config 1.5//EN", "jboss-ds_1_5.dtd");
        registerEntity("https://www.jboss.org/j2ee/schema/jboss-ds_5_0.xsd", "jboss-ds_5_0.xsd");
        // jboss-ra
        registerEntity("https://www.jboss.org/j2ee/schema/jboss-ra_1_0.xsd", "jboss-ra_1_0.xsd");

        // ejb
        registerEntity("-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 1.1//EN", "ejb-jar_1_1.dtd");
        registerEntity("-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 2.0//EN", "ejb-jar_2_0.dtd");
        registerEntity("http://java.sun.com/xml/ns/j2ee/ejb-jar_2_1.xsd", "ejb-jar_2_1.xsd");
        // ejb3
        registerEntity("http://java.sun.com/xml/ns/javaee/ejb-jar_3_0.xsd", "ejb-jar_3_0.xsd");
        registerEntity("http://java.sun.com/xml/ns/javaee/ejb-jar_3_1.xsd", "ejb-jar_3_1.xsd");
        registerEntity("http://xmlns.jcp.org/xml/ns/javaee/ejb-jar_3_2.xsd", "ejb-jar_3_2.xsd");
        registerEntity("https://jakarta.ee/xml/ns/jakartaee/ejb-jar_4_0.xsd", "ejb-jar_4_0.xsd");
        // jboss ejb2
        registerEntity("-//JBoss//DTD JBOSS//EN", "jboss.dtd");
        registerEntity("-//JBoss//DTD JBOSS 2.4//EN", "jboss_2_4.dtd");
        registerEntity("-//JBoss//DTD JBOSS 3.0//EN", "jboss_3_0.dtd");
        registerEntity("-//JBoss//DTD JBOSS 3.2//EN", "jboss_3_2.dtd");
        registerEntity("-//JBoss//DTD JBOSS 4.0//EN", "jboss_4_0.dtd");
        registerEntity("-//JBoss//DTD JBOSS 4.2//EN", "jboss_4_2.dtd");
        registerEntity("-//JBoss//DTD JBOSS 5.0//EN", "jboss_5_0.dtd");
        registerEntity("-//JBoss//DTD JBOSS 5.1.EAP//EN", "jboss_5_1_eap.dtd");
        registerEntity("-//JBoss//DTD JBOSS 6.0//EN", "jboss_6_0.dtd");
        registerEntity("-//JBoss//DTD JBOSSCMP-JDBC 3.0//EN", "jbosscmp-jdbc_3_0.dtd");
        registerEntity("-//JBoss//DTD JBOSSCMP-JDBC 3.2//EN", "jbosscmp-jdbc_3_2.dtd");
        registerEntity("-//JBoss//DTD JBOSSCMP-JDBC 4.0//EN", "jbosscmp-jdbc_4_0.dtd");
        registerEntity("-//JBoss//DTD JBOSSCMP-JDBC 4.2//EN", "jbosscmp-jdbc_4_2.dtd");
        // jboss ejb3
        registerEntity("https://www.jboss.org/j2ee/schema/jboss_5_0.xsd", "jboss_5_0.xsd");
        registerEntity("https://www.jboss.org/j2ee/schema/jboss_5_1.xsd", "jboss_5_1.xsd");

        // Java EE
        registerEntity("http://java.sun.com/xml/ns/j2ee/j2ee_1_4.xsd", "j2ee_1_4.xsd");
        registerEntity("http://java.sun.com/xml/ns/javaee/javaee_5.xsd", "javaee_5.xsd");
        registerEntity("http://java.sun.com/xml/ns/javaee/javaee_6.xsd", "javaee_6.xsd");
        registerEntity("http://xmlns.jcp.org/xml/ns/javaee/javaee_7.xsd", "javaee_7.xsd");
        registerEntity("http://xmlns.jcp.org/xml/ns/javaee/javaee_8.xsd", "javaee_8.xsd");
        registerEntity("https://jakarta.ee/xml/ns/jakartaee/jakartaee_9.xsd", "jakartaee_9.xsd");
        registerEntity("https://jakarta.ee/xml/ns/jakartaee/jakartaee_10.xsd", "jakartaee_10.xsd");
        // JBoss common
        registerEntity("https://www.jboss.org/j2ee/schema/jboss-common_5_1.xsd", "jboss-common_5_1.xsd");

        // Java EE WS
        registerEntity("http://schemas.xmlsoap.org/soap/encoding/", "soap-encoding_1_1.xsd");
        registerEntity("http://www.ibm.com/webservices/xsd/j2ee_web_services_client_1_1.xsd", "j2ee_web_services_client_1_1.xsd");
        registerEntity("http://www.ibm.com/webservices/xsd/j2ee_web_services_1_1.xsd", "j2ee_web_services_1_1.xsd");
        registerEntity("http://www.ibm.com/webservices/xsd/j2ee_jaxrpc_mapping_1_1.xsd", "j2ee_jaxrpc_mapping_1_1.xsd");
        registerEntity("http://java.sun.com/xml/ns/javaee/javaee_web_services_client_1_2.xsd", "javaee_web_services_client_1_2.xsd");
        registerEntity("http://java.sun.com/xml/ns/javaee/javaee_web_services_client_1_3.xsd", "javaee_web_services_client_1_3.xsd");
        registerEntity("http://java.sun.com/xml/ns/javaee/javaee_web_services_1_3.xsd", "javaee_web_services_1_3.xsd");
        registerEntity("http://xmlns.jcp.org/xml/ns/javaee/javaee_web_services_client_1_4.xsd", "javaee_web_services_client_1_4.xsd");
        registerEntity("http://xmlns.jcp.org/xml/ns/javaee/javaee_web_services_1_4.xsd", "javaee_web_services_1_4.xsd");
        registerEntity("https://jakarta.ee/xml/ns/jakartaee/jakartaee_web_services_client_2_0.xsd", "jakartaee_web_services_client_2_0.xsd");
        registerEntity("https://jakarta.ee/xml/ns/jakartaee/jakartaee_web_services_2_0.xsd", "jakartaee_web_services_2_0.xsd");
        registerEntity("https://jakarta.ee/xml/ns/jakartaee/akartaee_web_services_metadata_handler_3_0.xsd", "jakartaee_web_services_metadata_handler_3_0.xsd");

        // jsp
        registerEntity("http://java.sun.com/xml/ns/javaee/jsp_2_2.xsd", "jsp_2_2.xsd");
        registerEntity("http://xmlns.jcp.org/xml/ns/javaee/jsp_2_3.xsd", "jsp_2_3.xsd");
        registerEntity("https://jakarta.ee/xml/ns/jakartaee/jsp_3_0.xsd", "jsp_3_0.xsd");
        registerEntity("https://jakarta.ee/xml/ns/jakartaee/jsp_3_1.xsd", "jsp_3_1.xsd");

        // permissions
        registerEntity("http://xmlns.jcp.org/xml/ns/javaee/permissions_7.xsd", "permissions_7.xsd");
        registerEntity("https://jakarta.ee/xml/ns/jakartaee/permissions_9.xsd", "permissions_9.xsd");
        registerEntity("https://jakarta.ee/xml/ns/jakartaee/permissions_10.xsd", "permissions_10.xsd");

        // web
        registerEntity("-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN", "web-app_2_2.dtd");
        registerEntity("-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN", "web-app_2_3.dtd");
        registerEntity("http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd", "web-app_2_4.xsd");
        registerEntity("http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd", "web-app_2_5.xsd");
        registerEntity("http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd", "web-app_3_0.xsd");
        registerEntity("http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd", "web-app_3_1.xsd");
        registerEntity("http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd", "web-app_4_0.xsd");
        registerEntity("https://jakarta.ee/xml/ns/jakartaee/web-app_5_0.xsd", "web-app_5_0.xsd");
        registerEntity("https://jakarta.ee/xml/ns/jakartaee/web-app_6_0.xsd", "web-app_6_0.xsd");
        registerEntity("http://xmlns.jcp.org/xml/ns/javaee/web-common_3_0.xsd", "web-common_3_0.xsd");
        registerEntity("http://xmlns.jcp.org/xml/ns/javaee/web-common_3_1.xsd", "web-common_3_1.xsd");
        registerEntity("http://xmlns.jcp.org/xml/ns/javaee/web-common_4_0.xsd", "web-common_4_0.xsd");
        registerEntity("https://jakarta.ee/xml/ns/jakartaee/web-common_5_0.xsd", "web-common_5_0.xsd");
        registerEntity("https://jakarta.ee/xml/ns/jakartaee/web-common_6_0.xsd", "web-common_6_0.xsd");
        registerEntity("http://xmlns.jcp.org/xml/ns/javaee/web-facelettaglibrary_2_2.xsd", "web-facelettaglibrary_2_2.xsd");
        registerEntity("http://xmlns.jcp.org/xml/ns/javaee/web-facelettaglibrary_2_3.xsd", "web-facelettaglibrary_2_3.xsd");
        registerEntity("https://jakarta.ee/xml/ns/jakartaee/web-facelettaglibrary_3_0.xsd", "web-facelettaglibrary_3_0.xsd");
        registerEntity("https://jakarta.ee/xml/ns/jakartaee/web-facelettaglibrary_4_0.xsd", "web-facelettaglibrary_4_0.xsd");
        registerEntity("https://jakarta.ee/xml/ns/jakartaee/web-facelettaglibrary_4_1.xsd", "web-facelettaglibrary_4_1.xsd");
        registerEntity("http://xmlns.jcp.org/xml/ns/javaee/web-facesconfig_1_2.xsd", "web-facesconfig_1_2.xsd");
        registerEntity("http://xmlns.jcp.org/xml/ns/javaee/web-facesconfig_2_2.xsd", "web-facesconfig_2_2.xsd");
        registerEntity("https://jakarta.ee/xml/ns/jakartaee/web-facesconfig_3_0.xsd", "web-facesconfig_3_0.xsd");
        registerEntity("https://jakarta.ee/xml/ns/jakartaee/web-facesconfig_4_0.xsd", "web-facesconfig_4_0.xsd");
        registerEntity("https://jakarta.ee/xml/ns/jakartaee/web-facesconfig_4_1.xsd", "web-facesconfig_4_1.xsd");
        registerEntity("http://xmlns.jcp.org/xml/ns/javaee/web-fragment_3_0.xsd", "web-fragment_3_0.xsd");
        registerEntity("http://xmlns.jcp.org/xml/ns/javaee/web-fragment_3_1.xsd", "web-fragment_3_1.xsd");
        registerEntity("http://xmlns.jcp.org/xml/ns/javaee/web-fragment_4_0.xsd", "web-fragment_4_0.xsd");
        registerEntity("https://jakarta.ee/xml/ns/jakartaee/web-fragment_5_0.xsd", "web-fragment_5_0.xsd");
        registerEntity("https://jakarta.ee/xml/ns/jakartaee/web-fragment_6_0.xsd", "web-fragment_6_0.xsd");
        registerEntity("http://xmlns.jcp.org/xml/ns/javaee/web-jsptaglibrary_2_1.xsd", "web-jsptaglibrary_2_1.xsd");
        registerEntity("https://jakarta.ee/xml/ns/jakartaee/web-jsptaglibrary_3_0.xsd", "web-jsptaglibrary_3_0.xsd");
        registerEntity("https://jakarta.ee/xml/ns/jakartaee/web-jsptaglibrary_3_1.xsd", "web-jsptaglibrary_3_1.xsd");
        registerEntity("https://jakarta.ee/xml/ns/jakartaee/web-jsptaglibrary_4_0.xsd", "web-jsptaglibrary_4_0.xsd");
        registerEntity("http://xmlns.jcp.org/xml/ns/javaee/web-partialresponse_2_2.xsd", "web-partialresponse_2_2.xsd");
        registerEntity("http://xmlns.jcp.org/xml/ns/javaee/web-partialresponse_2_3.xsd", "web-partialresponse_2_3.xsd");
        registerEntity("https://jakarta.ee/xml/ns/jakartaee/web-partialresponse_3_0.xsd", "web-partialresponse_3_0.xsd");
        registerEntity("https://jakarta.ee/xml/ns/jakartaee/web-partialresponse_4_0.xsd", "web-partialresponse_4_0.xsd");
        registerEntity("https://jakarta.ee/xml/ns/jakartaee/web-partialresponse_4_1.xsd", "web-partialresponse_4_1.xsd");

        // jboss-web
        registerEntity("-//JBoss//DTD Web Application 2.2//EN", "jboss-web.dtd");
        registerEntity("-//JBoss//DTD Web Application 2.3//EN", "jboss-web_3_0.dtd");
        registerEntity("-//JBoss//DTD Web Application 2.3V2//EN", "jboss-web_3_2.dtd");
        registerEntity("-//JBoss//DTD Web Application 2.4//EN", "jboss-web_4_0.dtd");
        registerEntity("-//JBoss//DTD Web Application 4.2//EN", "jboss-web_4_2.dtd");
        registerEntity("-//JBoss//DTD Web Application 5.0//EN", "jboss-web_5_0.dtd");
        registerEntity("https://www.jboss.org/j2ee/schema/jboss-web_5_1.xsd", "jboss-web_5_1.xsd");

        // jboss-specific
        registerEntity("-//JBoss//DTD Web Service Reference 4.0//EN", "service-ref_4_0.dtd");
        registerEntity("-//JBoss//DTD Web Service Reference 4.2//EN", "service-ref_4_2.dtd");
        registerEntity("-//JBoss//DTD Web Service Reference 5.0//EN", "service-ref_5_0.dtd");
        registerEntity("-//JBoss//DTD MBean Service 3.2//EN", "jboss-service_3_2.dtd");
        registerEntity("-//JBoss//DTD MBean Service 4.0//EN", "jboss-service_4_0.dtd");
        registerEntity("-//JBoss//DTD MBean Service 4.2//EN", "jboss-service_4_2.dtd");
        registerEntity("-//JBoss//DTD MBean Service 5.0//EN", "jboss-service_5_0.dtd");
        registerEntity("-//JBoss//DTD JBOSS XMBEAN 1.0//EN", "jboss_xmbean_1_0.dtd");
        registerEntity("-//JBoss//DTD JBOSS XMBEAN 1.1//EN", "jboss_xmbean_1_1.dtd");
        registerEntity("-//JBoss//DTD JBOSS XMBEAN 1.2//EN", "jboss_xmbean_1_2.dtd");
        registerEntity("-//JBoss//DTD JBOSS Security Config 3.0//EN", "security_config.dtd");
        registerEntity("https://www.jboss.org/j2ee/schema/security-config_4_0.xsd", "security-config_4_0.xsd");
        registerEntity("urn:jboss:aop-deployer", "aop-deployer_1_1.xsd");
        registerEntity("urn:jboss:aop-beans:1.0", "aop-beans_1_0.xsd");
        registerEntity("urn:jboss:bean-deployer", "bean-deployer_1_0.xsd");
        registerEntity("urn:jboss:bean-deployer:2.0", "bean-deployer_2_0.xsd");
        registerEntity("urn:jboss:javabean:1.0", "javabean_1_0.xsd");
        registerEntity("urn:jboss:javabean:2.0", "javabean_2_0.xsd");
        registerEntity("urn:jboss:spring-beans:2.0", "mc-spring-beans_2_0.xsd");
        registerEntity("urn:jboss:policy:1.0", "policy_1_0.xsd");
        registerEntity("urn:jboss:osgi-beans:1.0", "osgi-beans_1_0.xsd");
        registerEntity("urn:jboss:seam-components:1.0", "seam-components_1_0.xsd");
        registerEntity("urn:jboss:security-config:4.1", "security-config_4_1.xsd");
        registerEntity("urn:jboss:security-config:5.0", "security-config_5_0.xsd");
        registerEntity("urn:jboss:jndi-binding-service:1.0", "jndi-binding-service_1_0.xsd");
        registerEntity("urn:jboss:jndi-binding-service:1.0.1", "jndi-binding-service_1_0_1.xsd");
        registerEntity("urn:jboss:user-roles:1.0", "user-roles_1_0.xsd");
        registerEntity("urn:jboss:user-roles:1.0.1", "user-roles_1_0_1.xsd");
    }

    /**
     * Obtain a read-only view of the current entity map.
     *
     * @return Map<String, String> of the publicID/systemID to dtd/schema file name
     */
    public static Map getEntityMap() {
        return Collections.unmodifiableMap(entities);
    }

    public static boolean isWarnOnNonFileURLs() {
        return warnOnNonFileURLs;
    }

    public static void setWarnOnNonFileURLs(boolean warnOnNonFileURLs) {
        XMLResourceResolver.warnOnNonFileURLs = warnOnNonFileURLs;
    }

    /**
     * Register the mapping from the public id/system id to the dtd/xsd file
     * name. This overwrites any existing mapping.
     *
     * @param id          the DOCTYPE public id or system id such as
     *                    "-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 1.1//EN"
     * @param dtdFileName the simple dtd/xsd file name, "ejb-jar.dtd"
     */
    public static void registerEntity(String id, String dtdFileName) {
        entities.put(id, dtdFileName);
    }


    /**
     * Register the mapping from the public id/system id to the dtd/xsd file
     * name. This overwrites any existing mapping.
     *
     * @param id          the DOCTYPE public id or system id such as
     *                    "-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 1.1//EN"
     * @param dtdOrSchema the simple dtd/xsd file name, "ejb-jar.dtd"
     */
    public synchronized void registerLocalEntity(String id, String dtdOrSchema) {
        if (localEntities == null)
            localEntities = new ConcurrentHashMap();
        localEntities.put(id, dtdOrSchema);
    }

    /**
     * Returns DTD/Schema inputSource. The resolution logic is:
     * <p/>
     * 1. Check the publicId against the current registered values in the class
     * mapping of entity name to dtd/schema file name. If found, the resulting
     * file name is passed to the loadResource to locate the file as a
     * classpath resource.
     * <p/>
     * 2. Check the systemId against the current registered values in the class
     * mapping of entity name to dtd/schema file name. If found, the resulting
     * file name is passed to the loadResource to locate the file as a
     * classpath resource.
     * <p/>
     * 3. Strip the systemId name down to the simple file name by removing an URL
     * style path elements (myschemas/x.dtd becomes x.dtd), and call
     * loadResource to locate the simple file name as a classpath resource.
     * <p/>
     * 4. Attempt to resolve the systemId as a URL from which the schema can be
     * read. If the URL input stream can be opened this returned as the resolved
     * input.
     *
     * @param publicId - Public ID of DTD, or null if it is a schema
     * @param systemId - the system ID of DTD or Schema
     * @return InputSource of entity
     */
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        entityResolved.set(Boolean.FALSE);

        // nothing to resolve
        if (publicId == null && systemId == null)
            return null;

        boolean trace = log.isTraceEnabled();

        boolean resolvePublicIdFirst = true;
        if (publicId != null && systemId != null) {
            String registeredSystemId = null;
            if (localEntities != null)
                registeredSystemId = (String) localEntities.get(publicId);
            if (registeredSystemId == null)
                registeredSystemId = (String) entities.get(publicId);

            if (registeredSystemId != null && !registeredSystemId.equals(systemId)) {
                resolvePublicIdFirst = false;
                if (trace)
                    log.trace("systemId argument '" + systemId + "' for publicId '" +
                            publicId + "' is different from the registered systemId '" +
                            registeredSystemId + "', resolution will be based on the argument");
            }
        }

        InputSource inputSource = null;

        if (resolvePublicIdFirst) {
            // Look for a registered publicID
            inputSource = resolvePublicID(publicId, trace);
        }

        if (inputSource == null) {
            // Try to resolve the systemID from the registry
            inputSource = resolveSystemID(systemId, trace);
        }

        if (inputSource == null) {
            // Try to resolve the systemID as a classpath reference under dtd or schema
            inputSource = resolveClasspathName(systemId, trace);
        }

        if (inputSource == null) {
            // Try to resolve the systemID as a absolute URL
            inputSource = resolveSystemIDasURL(systemId, trace);
        }

        entityResolved.set(new Boolean(inputSource != null));

        if (inputSource == null)
            log.debug("Cannot resolve [publicID=" + publicId + ",systemID=" + systemId + "]");

        return inputSource;
    }

    /**
     * Returns the boolean value to inform id DTD was found in the XML file or not
     * <p/>
     * This is here to avoid validation errors in descriptors that do not have a DOCTYPE declaration.
     *
     * @return boolean - true if DTD was found in XML
     */
    public boolean isEntityResolved() {
        Boolean value = entityResolved.get();
        return value != null ? value.booleanValue() : false;
    }

    /**
     * Load the schema from the class entity to schema file mapping.
     *
     * @param publicId - the public entity name of the schema
     * @param trace    - trace level logging flag
     * @return the InputSource for the schema file found on the classpath, null
     *         if the publicId is not registered or found.
     * @see #registerEntity(String, String)
     */
    protected InputSource resolvePublicID(String publicId, boolean trace) {
        if (publicId == null)
            return null;

        if (trace)
            log.trace("resolvePublicID, publicId=" + publicId);

        InputSource inputSource = null;

        String filename = null;
        if (localEntities != null)
            filename = (String) localEntities.get(publicId);
        if (filename == null)
            filename = (String) entities.get(publicId);

        if (filename != null) {
            if (trace)
                log.trace("Found entity from publicId=" + publicId + " fileName=" + filename);

            InputStream ins = loadResource(filename, trace);
            if (ins != null) {
                inputSource = new InputSource(ins);
                inputSource.setPublicId(publicId);
            } else {
                log.trace("Cannot load publicId from classpath resource: " + filename);

                // Try the file name as a URI
                inputSource = resolveSystemIDasURL(filename, trace);

                if (inputSource == null)
                    log.warn("Cannot load publicId from resource: " + filename);
            }
        }

        return inputSource;
    }

    /**
     * Attempt to use the systemId as a URL from which the schema can be read. This
     * checks to see whether the systemId is a key to an entry in the class
     * entity map.
     *
     * @param systemId - the systemId
     * @param trace    - trace level logging flag
     * @return the URL InputSource if the URL input stream can be opened, null
     *         if the systemId is not a URL or could not be opened.
     */
    protected InputSource resolveSystemID(String systemId, boolean trace) {
        if (systemId == null)
            return null;

        if (trace)
            log.trace("resolveSystemID, systemId=" + systemId);

        InputSource inputSource = null;

        // Try to resolve the systemId as an entity key
        String filename = null;
        if (localEntities != null)
            filename = (String) localEntities.get(systemId);
        if (filename == null)
            filename = (String) entities.get(systemId);

        if (filename != null) {
            if (trace)
                log.trace("Found entity systemId=" + systemId + " fileName=" + filename);

            InputStream ins = loadResource(filename, trace);
            if (ins != null) {
                inputSource = new InputSource(ins);
                inputSource.setSystemId(systemId);
            } else {
                log.warn("Cannot load systemId from resource: " + filename);
            }
        }

        return inputSource;
    }

    /**
     * Attempt to use the systemId as a URL from which the schema can be read. This
     * uses the systemID as a URL.
     *
     * @param systemId - the systemId
     * @param trace    - trace level logging flag
     * @return the URL InputSource if the URL input stream can be opened, null
     *         if the systemId is not a URL or could not be opened.
     */
    protected InputSource resolveSystemIDasURL(String systemId, boolean trace) {
        if (systemId == null)
            return null;

        if (trace)
            log.trace("resolveSystemIDasURL, systemId=" + systemId);

        InputSource inputSource = null;

        // Try to use the systemId as a URL to the schema
        try {
            if (trace)
                log.trace("Trying to resolve systemId as a URL");

            URL url = new URL(systemId);
            if (warnOnNonFileURLs && url.getProtocol().equalsIgnoreCase("file") == false) {
                log.warn("Trying to resolve systemId as a non-file URL: " + systemId);
            }

            InputStream ins = url.openStream();
            if (ins != null) {
                inputSource = new InputSource(ins);
                inputSource.setSystemId(systemId);
            } else {
                log.warn("Cannot load systemId as URL: " + systemId);
            }

            if (trace)
                log.trace("Resolved systemId as a URL");
        } catch (MalformedURLException ignored) {
            if (trace)
                log.trace("SystemId is not a url: " + systemId, ignored);
        } catch (IOException e) {
            if (trace)
                log.trace("Failed to obtain URL.InputStream from systemId: " + systemId, e);
        }
        return inputSource;
    }

    /**
     * Resolve the systemId as a classpath resource. If not found, the
     * systemId is simply used as a classpath resource name.
     *
     * @param systemId - the system ID of DTD or Schema
     * @param trace    - trace level logging flag
     * @return the InputSource for the schema file found on the classpath, null
     *         if the systemId is not registered or found.
     */
    protected InputSource resolveClasspathName(String systemId, boolean trace) {
        if (systemId == null)
            return null;

        if (trace)
            log.trace("resolveClasspathName, systemId=" + systemId);
        String filename = systemId;
        // Parse the systemId as a uri to get the final path component
        try {
            URI url = new URI(systemId);
            String path = url.getPath();
            if (path == null)
                path = url.getSchemeSpecificPart();
            int slash = path.lastIndexOf('/');
            if (slash >= 0)
                filename = path.substring(slash + 1);
            else
                filename = path;

            if (filename.length() == 0)
                return null;

            if (trace)
                log.trace("Mapped systemId to filename: " + filename);
        } catch (URISyntaxException e) {
            if (trace)
                log.trace("systemId: is not a URI, using systemId as resource", e);
        }

        // Resolve the filename as a classpath resource
        InputStream is = loadResource(filename, trace);
        InputSource inputSource = null;
        if (is != null) {
            inputSource = new InputSource(is);
            inputSource.setSystemId(systemId);
        }
        return inputSource;
    }

    /**
     * Look for the resource on the class loader that loaded this resolver.
     * If not found try the thread context class loader.
     * <p/>
     * This first simply tries the resource name as is, and if not found, the resource
     * is prepended with either "dtd/" or "schema/" depending on whether the
     * resource ends in ".dtd" or ".xsd".
     *
     * @param resource - the classpath resource name of the schema
     * @param trace    - trace level logging flag
     * @return the resource InputStream if found, null if not found.
     */
    protected InputStream loadResource(String resource, boolean trace) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = loadResource(classLoader, resource, trace);
        if (inputStream == null) {
            classLoader = Thread.currentThread().getContextClassLoader();
            inputStream = loadResource(classLoader, resource, trace);
        }
        return inputStream;
    }

    /**
     * Look for the resource on the given class loader.
     * <p/>
     * This first simply tries the resource name as is, and if not found, the resource
     * is prepended with either "dtd/" or "schema/" depending on whether the
     * resource ends in ".dtd" or ".xsd".
     *
     * @param resource - the classpath resource name of the schema
     * @param trace    - trace level logging flag
     * @return the resource InputStream if found, null if not found.
     */
    protected InputStream loadResource(ClassLoader loader, String resource, boolean trace) {
        URL url = loader.getResource(resource);
        if (url == null) {
            /* Prefix the simple filename with the schema type patch as this is the
               naming convention for the jboss bundled schemas.
            */
            if (resource.endsWith(".dtd"))
                resource = "dtd/" + resource;
            else if (resource.endsWith(".xsd"))
                resource = "schema/" + resource;
            url = loader.getResource(resource);
        }

        InputStream inputStream = null;
        if (url != null) {
            if (trace)
                log.trace(resource + " maps to URL: " + url);
            try {
                inputStream = url.openStream();
            } catch (IOException e) {
                log.debug("Failed to open url stream", e);
            }
        }
        return inputStream;
    }

    public Object resolveEntity(String publicID, String systemID, String baseURI, String namespace) throws XMLStreamException {
        InputSource inputSource = null;
        try {
            inputSource = resolveEntity(publicID, systemID);
        } catch (Exception e) {
            log.debug("Failed to resolve entity", e);
        }
        return inputSource != null ? inputSource.getByteStream() : null;
    }

    public LSInput resolveResource(String type, String namespaceURI, String publicID, String systemID, String baseURI) {
        InputSource inputSource = null;
        try {
            inputSource = resolveEntity(publicID, systemID);
        } catch (Exception e) {
            log.debug("Failed to resolve resource", e);
        }
        LSInput result = null;
        if (inputSource != null) {
            result = new LSInputImpl(publicID, systemID, baseURI, inputSource);
        }
        return result;
    }

    private static class LSInputImpl implements LSInput {

        private final String systemID;
        private final String publicID;
        private final String baseURI;
        private final InputSource inputSource;

        LSInputImpl(String publicID, String systemID, String baseURI, InputSource inputSource) {
            this.inputSource = inputSource;
            this.systemID = systemID;
            this.publicID = publicID;
            this.baseURI = baseURI;
        }

        public Reader getCharacterStream() {
            return inputSource.getCharacterStream();
        }

        public void setCharacterStream(Reader characterStream) {
        }

        public InputStream getByteStream() {
            return inputSource.getByteStream();
        }

        public void setByteStream(InputStream byteStream) {
        }

        public String getStringData() {
            return null;
        }

        public void setStringData(String stringData) {
        }

        public String getSystemId() {
            return systemID;
        }

        public void setSystemId(String systemId) {
        }

        public String getPublicId() {
            return publicID;
        }

        public void setPublicId(String publicId) {
        }

        public String getBaseURI() {
            return baseURI;
        }

        public void setBaseURI(String baseURI) {
        }

        public String getEncoding() {
            return null;
        }

        public void setEncoding(String encoding) {
        }

        public boolean getCertifiedText() {
            return false;
        }

        public void setCertifiedText(boolean certifiedText) {
        }
    }
}
