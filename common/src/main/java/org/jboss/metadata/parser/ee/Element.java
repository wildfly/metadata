/*
 * Copyright The JBoss Metadata Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.jboss.metadata.parser.ee;

import java.util.HashMap;
import java.util.Map;

/**
 * An enumeration of all the possible XML elements in the EE schema, by name.
 *
 * @author Remy Maucherat
 * @author Eduardo Martins
 */
public enum Element {
    // must be first
    UNKNOWN(null),

    // javaee 6 & 7 elements in alpha order
    ADDRESSING("addressing"),
    ADMINISTERED_OBJECT("administered-object"),

    CLASS_NAME("class-name"),
    CLEARED("cleared"),
    CLIENT_ID("client-id"),
    CONNECTION_FACTORY("connection-factory"),
    CONFIG_NAME("config-name"),
    CONFIG_FILE("config-file"),
    CONTEXT_SERVICE("context-service"),
    CONTEXT_SERVICE_REF("context-service-ref"),

    DATA_SOURCE("data-source"),
    DATABASE_NAME("database-name"),
    DESCRIPTION("description"),
    DESTINATION_NAME("destination-name"),
    DISPLAY_NAME("display-name"),

    EJB_LINK("ejb-link"),
    EJB_LOCAL_REF("ejb-local-ref"),
    EJB_REF("ejb-ref"),
    EJB_REF_NAME("ejb-ref-name"),
    EJB_REF_TYPE("ejb-ref-type"),
    ENABLE_MTOM("enable-mtom"),
    ENABLED("enabled"),
    ENV_ENTRY("env-entry"),
    ENV_ENTRY_NAME("env-entry-name"),
    ENV_ENTRY_TYPE("env-entry-type"),
    ENV_ENTRY_VALUE("env-entry-value"),

    FROM("from"),

    HANDLER("handler"),
    HANDLER_CHAIN("handler-chain"),
    HANDLER_CHAINS("handler-chains"),
    HANDLER_CLASS("handler-class"),
    HANDLER_NAME("handler-name"),
    HOME("home"),
    HOST("host"),
    HUNG_TASK_THRESHOLD("hung-task-threshold"),

    ICON("icon"),
    IGNORE_DEPENDECY("ignore-dependency"),
    INIT_PARAM("init-param"),
    INITAL_POOL_SIZE("initial-pool-size"),
    INJECTION_TARGET("injection-target"),
    INJECTION_TARGET_CLASS("injection-target-class"),
    INJECTION_TARGET_NAME("injection-target-name"),
    INTERFACE_NAME("interface-name"),
    ISOLATION_LEVEL("isolation-level"),

    JAXRPC_MAPPING_FILE("jaxrpc-mapping-file"),
    JMS_CONNECTION_FACTORY("jms-connection-factory"),
    JMS_DESTINATION("jms-destination"),
    JNDI_NAME("jndi-name"),

    LARGE_ICON("large-icon"),
    LIFECYCLE_CALLBACK_CLASS("lifecycle-callback-class"),
    LIFECYCLE_CALLBACK_METHOD("lifecycle-callback-method"),
    LOCAL("local"),
    LOCAL_HOME("local-home"),
    LOCAL_JNDI_NAME("local-jndi-name"),
    LOGIN_TIMEOUT("login-timeout"),
    LOOKUP_NAME("lookup-name"),

    MAIL_SESSION("mail-session"),
    MANAGED_EXECUTOR("managed-executor"),
    MANAGED_SCHEDULED_EXECUTOR("managed-scheduled-executor"),
    MANAGED_THREAD_FACTORY("managed-thread-factory"),

    MAPPED_NAME("mapped-name"),
    MAX_ASYNC("max-async"),
    MAX_IDLE_TIME("max-idle-time"),
    MAX_POOL_SIZE("max-pool-size"),
    MAX_STATEMENTS("max-statements"),
    MIN_POOL_SIZE("min-pool-size"),
    MESSAGE_DESTINATION("message-destination"),
    MESSAGE_DESTINATION_LINK("message-destination-link"),
    MESSAGE_DESTINATION_NAME("message-destination-name"),
    MESSAGE_DESTINATION_REF("message-destination-ref"),
    MESSAGE_DESTINATION_REF_NAME("message-destination-ref-name"),
    MESSAGE_DESTINATION_TYPE("message-destination-type"),
    MESSAGE_DESTINATION_USAGE("message-destination-usage"),
    MTOM_THRESHOLD("mtom-threshold"),

    NAME("name"),

    PARAM_NAME("param-name"),
    PARAM_VALUE("param-value"),
    PASSWORD("password"),
    PERSISTENCE_CONTEXT_REF("persistence-context-ref"),
    PERSISTENCE_CONTEXT_REF_NAME("persistence-context-ref-name"),
    PERSISTENCE_CONTEXT_TYPE("persistence-context-type"),
    PERSISTENCE_CONTEXT_SYNCHRONIZATION("persistence-context-synchronization"),
    PERSISTENCE_PROPERTY("persistence-property"),
    PERSISTENCE_UNIT_NAME("persistence-unit-name"),
    PERSISTENCE_UNIT_REF("persistence-unit-ref"),
    PERSISTENCE_UNIT_REF_NAME("persistence-unit-ref-name"),
    PORT_COMPONENT_LINK("port-component-link"),
    PORT_COMPONENT_REF("port-component-ref"),
    PORT_NAME("port-name"),
    PORT_NAME_PATTERN("port-name-pattern"),
    PORT_NUMBER("port-number"),
    PORT_QNAME("port-qname"),
    POST_CONSTRUCT("post-construct"),
    PROP_NAME("prop-name"),
    PROP_VALUE("prop-value"),
    PRE_DESTROY("pre-destroy"),
    PRINCIPAL_NAME("principal-name"),
    PRIORITY("priority"),
    PROPAGATED("propagated"),
    PROPERTY("property"),
    PROTOCOL_BINDINGS("protocol-bindings"),

    QUALIFIER("qualifier"),

    REMOTE("remote"),
    REQUIRED("required"),
    RES_AUTH("res-auth"),
    RES_REF_NAME("res-ref-name"),
    RES_SHARING_SCOPE("res-sharing-scope"),
    RES_TYPE("res-type"),
    RES_URL("res-url"),
    RESOURCE_ADAPTER("resource-adapter"),
    RESOURCE_ENV_REF("resource-env-ref"),
    RESOURCE_ENV_REF_NAME("resource-env-ref-name"),
    RESOURCE_ENV_REF_TYPE("resource-env-ref-type"),
    RESOURCE_NAME("resource-name"),
    RESOURCE_REF("resource-ref"),
    RESPECT_BINDING("respect-binding"),
    RESPONSES("responses"),
    ROLE_LINK("role-link"),
    ROLE_NAME("role-name"),

    SERVER_NAME("server-name"),
    SERVICE_ENDPOINT_INTERFACE("service-endpoint-interface"),
    SERVICE_INTERFACE("service-interface"),
    SERVICE_IMPL_CLASS("service-impl-class"),
    SERVICE_NAME_PATTERN("service-name-pattern"),
    SERVICE_QNAME("service-qname"),
    SERVICE_REF("service-ref"),
    SERVICE_REF_NAME("service-ref-name"),
    SERVICE_REF_TYPE("service-ref-type"),
    SMALL_ICON("small-icon"),
    SOAP_HEADER("soap-header"),
    SOAP_ROLE("soap-role"),
    STORE_PROTOCOL("store-protocol"),
    STORE_PROTOCOL_CLASS("store-protocol-class"),
    STUB_PROPERTY("stub-property"),

    TRANSACTION_SUPPORT("transaction-support"),
    TRANSACTIONAL("transactional"),
    TRANSPORT_PROTOCOL("transport-protocol"),
    TRANSPORT_PROTOCOL_CLASS("transport-protocol-class"),

    UNCHANGED("unchanged"),
    URL("url"),
    USER("user"),

    VALUE("value"),
    VIRTUAL("virtual"),

    WSDL_FILE("wsdl-file"),
    WSDL_OVERRIDE("wsdl-override"),;

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
