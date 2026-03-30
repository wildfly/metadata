/*
 * Copyright The JBoss Metadata Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.jboss.metadata.ear.parser.jboss;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.jboss.logging.Logger;
import org.jboss.metadata.ear.jboss.JBossAppMetaData;
import org.jboss.metadata.ear.jboss.ServiceModuleMetaData;
import org.jboss.metadata.ear.spec.EarEnvironmentRefsGroupMetaData;
import org.jboss.metadata.ear.spec.ModuleMetaData;
import org.jboss.metadata.ear.spec.ModulesMetaData;
import org.jboss.metadata.ear.spec.WebModuleMetaData;
import org.jboss.metadata.javaee.spec.DescriptionGroupMetaData;
import org.jboss.metadata.javaee.spec.MessageDestinationsMetaData;
import org.jboss.metadata.javaee.spec.SecurityRolesMetaData;
import org.jboss.metadata.parser.ee.SecurityRoleMetaDataParser;
import org.jboss.metadata.ear.parser.spec.EarMetaDataParser;
import org.jboss.metadata.property.PropertyReplacer;
import org.jboss.metadata.property.PropertyReplacers;

/**
 * @author John Bailey
 */
public class JBossAppMetaDataParser extends EarMetaDataParser {

    private static final Logger logger = Logger.getLogger(JBossAppMetaDataParser.class);

    public static final JBossAppMetaDataParser INSTANCE = new JBossAppMetaDataParser();

    public JBossAppMetaData parse(final XMLStreamReader reader) throws XMLStreamException {
        return parse(reader, PropertyReplacers.noop());
    }


    public JBossAppMetaData parse(final XMLStreamReader reader, final PropertyReplacer propertyReplacer) throws XMLStreamException {
        final EarMetaDataParser earMetaDataParser = new EarMetaDataParser();
        reader.require(START_DOCUMENT, null, null);

        // Read until the first start element
        Version version = null;
        while (reader.hasNext() && reader.next() != START_ELEMENT) {
            if (reader.getEventType() == DTD) {
                final String dtdLocation = readDTDLocation(reader);
                if (dtdLocation != null) {
                    version = Version.forLocation(dtdLocation);
                }
            }
        }
        final String schemaLocation = readSchemaLocation(reader);
        if (schemaLocation != null) {
            version = Version.forLocation(schemaLocation);
        }

        if (version == null || Version.UNKNOWN.equals(version)) {
            version = Version.APP_7_0;
        }

        JBossAppMetaData appMetaData = new JBossAppMetaData();

        // Handle attributes
        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            final String value = reader.getAttributeValue(i);
            if (attributeHasNamespace(reader, i)) {
                continue;
            }
            final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
            switch (attribute) {
                case ID: {
                    appMetaData.setId(value);
                    break;
                }
                case VERSION: {
                    break;
                }
                default:
                    throw unexpectedAttribute(reader, i);
            }
        }

        //TODO: hn

        final EarEnvironmentRefsGroupMetaData environmentRefsGroupMetaData = new EarEnvironmentRefsGroupMetaData();
        appMetaData.setDescriptionGroup(new DescriptionGroupMetaData());
        appMetaData.setModules(new ModulesMetaData());
        appMetaData.setSecurityRoles(new SecurityRolesMetaData());
        environmentRefsGroupMetaData.setMessageDestinations(new MessageDestinationsMetaData());
        appMetaData.setEarEnvironmentRefsGroup(environmentRefsGroupMetaData);
        while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            final Element element = Element.forName(reader.getLocalName());
            Namespace namespace = Namespace.forUri(reader.getNamespaceURI());
            if (namespace == Namespace.SPEC || namespace == Namespace.SPEC_7_0 || namespace == Namespace.JAKARTAEE) {
                super.handleElement(reader, appMetaData, propertyReplacer);
            } else {
                switch (element) {
                    case DISTINCT_NAME: {
                        final String val = getElementText(reader, propertyReplacer);
                        appMetaData.setDistinctName(val);
                        break;
                    }
                    case MODULE_ORDER: {
                        logger.warn("module-order element in jboss-app.xml is deprecated and has been ignored");
                        consumeElementContent(reader);
                        break;
                    }
                    case SECURITY_DOMAIN: {
                        appMetaData.setSecurityDomain(getElementText(reader, propertyReplacer));
                        break;
                    }
                    case UNAUTHENTICATED_PRINCIPAL: {
                        appMetaData.setUnauthenticatedPrincipal(getElementText(reader, propertyReplacer));
                        break;
                    }
                    case JMX_NAME: {
                        logger.warn("jmx-name element in jboss-app.xml is deprecated and has been ignored");
                        consumeElementContent(reader);
                        break;
                    }
                    case LIBRARY_DIRECTORY: {
                        appMetaData.setLibraryDirectory(getElementText(reader, propertyReplacer));
                        break;
                    }
                    case LIMIT_APPCLIENT_MODULES: {
                        appMetaData.setLimitAppclientModules(Boolean.parseBoolean(getElementText(reader, propertyReplacer)));
                        break;
                    }
                    case LOADER_REPOSITORY: {
                        logger.warn("loader-repository element in jboss-app.xml is deprecated and has been ignored");
                        parseLoaderRepository(reader, propertyReplacer);
                        break;
                    }
                    case MODULE: {
                        appMetaData.getModules().add(parseModule(reader, propertyReplacer));
                        break;
                    }
                    case SECURITY_ROLE: {
                        appMetaData.getSecurityRoles().add(SecurityRoleMetaDataParser.parse(reader, propertyReplacer));
                        break;
                    }
                    default:
                        throw unexpectedElement(reader);
                }
            }
        }

        return appMetaData;
    }

    private static void parseLoaderRepository(XMLStreamReader reader, PropertyReplacer propertyReplacer) throws XMLStreamException {
        //we still accept this for legacy reasons
        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            final String value = reader.getAttributeValue(i);
            if (attributeHasNamespace(reader, i)) {
                continue;
            }
            final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
            switch (attribute) {
                case ID: {
                    //ignore
                    break;
                }
                case LOADER_REPOSITORY_CLASS: {
                    //ignore
                }
                default:
                    throw unexpectedAttribute(reader, i);
            }
        }

        while (reader.hasNext() && reader.next() != END_ELEMENT) {
            if (reader.hasText()) {
                final StringBuilder builder = new StringBuilder();
                int event = reader.getEventType();
                // while (event != END_ELEMENT) {
                if (event == XMLStreamConstants.CHARACTERS
                        || event == XMLStreamConstants.CDATA
                        || event == XMLStreamConstants.SPACE
                        || event == XMLStreamConstants.ENTITY_REFERENCE) {
                    builder.append(reader.getText());
                } else if (event == XMLStreamConstants.PROCESSING_INSTRUCTION || event == XMLStreamConstants.COMMENT) {
                    // Skips (not kept).
                } else if (event == XMLStreamConstants.END_DOCUMENT) {
                    throw new XMLStreamException(
                            "Unexpected end of document when reading element text content",
                            reader.getLocation());
                } else if (event == XMLStreamConstants.START_ELEMENT) {
                    throw new XMLStreamException(
                            "Element text content may not contain START_ELEMENT",
                            reader.getLocation());
                } else {
                    throw new XMLStreamException("Unexpected event type "
                            + event, reader.getLocation());
                }
                // }
            } else if (reader.isStartElement()) {
                final Element element = Element.forName(reader.getLocalName());
                switch (element) {
                    case LOADER_REPOSITORY_CONFIG: {
                        parseLoaderRepositoryConfig(reader, propertyReplacer);
                        break;
                    }
                }
            } else {
                throw unexpectedElement(reader);
            }
        }
    }

    private static void parseLoaderRepositoryConfig(XMLStreamReader reader, final PropertyReplacer propertyReplacer) throws XMLStreamException {
        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            reader.getAttributeValue(i);
            if (attributeHasNamespace(reader, i)) {
                continue;
            }
            final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
            switch (attribute) {
                case ID: {
                    break;
                }
                case CONFIG_PARSER_CLASS: {
                }
                default:
                    throw unexpectedAttribute(reader, i);
            }
        }
        getElementText(reader, propertyReplacer);
    }


    private static ModuleMetaData parseModule(XMLStreamReader reader, final PropertyReplacer propertyReplacer) throws XMLStreamException {
        final ModuleMetaData moduleMetaData = new ModuleMetaData();

        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            final String value = reader.getAttributeValue(i);
            if (attributeHasNamespace(reader, i)) {
                continue;
            }
            final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
            switch (attribute) {
                case ID: {
                    moduleMetaData.setId(value);
                    break;
                }
                default:
                    throw unexpectedAttribute(reader, i);
            }
        }

        while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            final Element element = Element.forName(reader.getLocalName());
            switch (element) {
                case HAR:
                    moduleMetaData.setValue(parseService(reader, propertyReplacer));
                    break;
                case SERVICE:
                    moduleMetaData.setValue(parseService(reader, propertyReplacer));
                    break;
                case WEB:
                    moduleMetaData.setValue(parseWeb(reader, propertyReplacer));
                    break;
                default:
                    throw unexpectedElement(reader);
            }
        }
        return moduleMetaData;
    }

    private static ServiceModuleMetaData parseService(final XMLStreamReader reader, final PropertyReplacer propertyReplacer) throws XMLStreamException {
        final ServiceModuleMetaData module = new ServiceModuleMetaData();
        module.setFileName(getElementText(reader, propertyReplacer));
        return module;
    }

    private static WebModuleMetaData parseWeb(final XMLStreamReader reader, final PropertyReplacer propertyReplacer) throws XMLStreamException {
        final WebModuleMetaData webModuleMetaData = new WebModuleMetaData();
        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            final String value = reader.getAttributeValue(i);
            if (attributeHasNamespace(reader, i)) {
                continue;
            }
            final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
            switch (attribute) {
                case ID: {
                    webModuleMetaData.setId(value);
                    break;
                }
                default:
                    throw unexpectedAttribute(reader, i);
            }
        }

        while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            final org.jboss.metadata.ear.parser.spec.Element element = org.jboss.metadata.ear.parser.spec.Element.forName(reader.getLocalName());
            switch (element) {
                case CONTEXT_ROOT:
                    webModuleMetaData.setContextRoot(getElementText(reader, propertyReplacer));
                    break;
                case WEB_URI:
                    webModuleMetaData.setWebURI(getElementText(reader, propertyReplacer));
                    break;
                default:
                    throw unexpectedElement(reader);
            }
        }
        return webModuleMetaData;
    }
}
