/*
 * Copyright The JBoss Metadata Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.jboss.metadata.ear.parser.spec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.jboss.metadata.ear.spec.EarEnvironmentRefsGroupMetaData;
import org.jboss.metadata.ear.spec.EarMetaData;
import org.jboss.metadata.ear.spec.EarVersion;
import org.jboss.metadata.ear.spec.ModulesMetaData;
import org.jboss.metadata.javaee.spec.DescriptionGroupMetaData;
import org.jboss.metadata.javaee.spec.MessageDestinationsMetaData;
import org.jboss.metadata.javaee.spec.SecurityRolesMetaData;
import org.jboss.metadata.parser.ee.DescriptionGroupMetaDataParser;
import org.jboss.metadata.parser.ee.EnvironmentRefsGroupMetaDataParser;
import org.jboss.metadata.parser.ee.MessageDestinationMetaDataParser;
import org.jboss.metadata.parser.ee.SecurityRoleMetaDataParser;
import org.jboss.metadata.parser.util.MetaDataElementParser;
import org.jboss.metadata.property.PropertyReplacer;
import org.jboss.metadata.property.PropertyReplacers;
import org.jboss.logging.Logger;

/**
 * @author John Bailey
 */
public class EarMetaDataParser extends MetaDataElementParser {

    private static final Logger logger = Logger.getLogger(EarMetaDataParser.class);

    public static final EarMetaDataParser INSTANCE = new EarMetaDataParser();

    public EarMetaData parse(final XMLStreamReader reader) throws XMLStreamException {
        return parse(reader, PropertyReplacers.noop());
    }

    public EarMetaData parse(final XMLStreamReader reader, final PropertyReplacer propertyReplacer) throws XMLStreamException {
        reader.require(START_DOCUMENT, null, null);

        // Read until the first start element
        EarVersion version = null;
        while (reader.hasNext() && reader.next() != START_ELEMENT) {
            if (reader.getEventType() == DTD) {
                final String dtdLocation = readDTDLocation(reader);
                if (dtdLocation != null) {
                    version = EarVersion.forLocation(dtdLocation);
                }
            }
        }
        final String schemaLocation = readSchemaLocation(reader);
        if (schemaLocation != null) {
            version = EarVersion.forLocation(schemaLocation);
        }

        if (version == null || EarVersion.UNKNOWN.equals(version)) {
            // Look at the version attribute
            String versionString = null;
            final int count = reader.getAttributeCount();
            for (int i = 0; i < count; i++) {
                if (attributeHasNamespace(reader, i)) {
                    continue;
                }
                final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
                if (attribute == Attribute.VERSION) {
                    versionString = reader.getAttributeValue(i);
                }
            }
            // Parse version string if present
            if (versionString != null) {
                switch (versionString) {
                    case "1.4":
                        version = EarVersion.APP_1_4;
                        break;
                    case "5":
                        version = EarVersion.APP_5_0;
                        break;
                    case "6":
                        version = EarVersion.APP_6_0;
                        break;
                    case "7":
                        version = EarVersion.APP_7_0;
                        break;
                    case "8":
                        version = EarVersion.APP_8_0;
                        break;
                    case "9":
                        version = EarVersion.APP_9_0;
                        break;
                    case "10":
                        version = EarVersion.APP_10_0;
                        break;
                    case "11":
                        version = EarVersion.APP_11_0;
                        break;
                    default:
                        logger.warnf("Unsupported or unknown EAR version '%s' specified in descriptor. Defaulting to version 6.0", versionString);
                        break;
                }
            }
        }

        if (version == null || EarVersion.UNKNOWN.equals(version)) {
            version = EarVersion.APP_6_0;
        }

        final EarMetaData earMetaData = new EarMetaData(version);


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
                    earMetaData.setId(value);
                    break;
                }
                case VERSION: {
                    break;
                }
                default:
                    throw unexpectedAttribute(reader, i);
            }
        }

        // Handler Attributes

        final EarEnvironmentRefsGroupMetaData environmentRefsGroupMetaData = new EarEnvironmentRefsGroupMetaData();
        earMetaData.setDescriptionGroup(new DescriptionGroupMetaData());
        earMetaData.setModules(new ModulesMetaData());
        earMetaData.setSecurityRoles(new SecurityRolesMetaData());
        environmentRefsGroupMetaData.setMessageDestinations(new MessageDestinationsMetaData());
        earMetaData.setEarEnvironmentRefsGroup(environmentRefsGroupMetaData);

        // Handle elements
        while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            handleElement(reader, earMetaData, propertyReplacer);
        }

        return earMetaData;
    }

    protected void handleElement(XMLStreamReader reader, EarMetaData earMetaData, final PropertyReplacer propertyReplacer) throws XMLStreamException {
        if (DescriptionGroupMetaDataParser.parse(reader, earMetaData.getDescriptionGroup())) {
            return;
        }
        if (EnvironmentRefsGroupMetaDataParser.parse(reader, earMetaData.getEarEnvironmentRefsGroup(), propertyReplacer)) {
            return;
        }
        final Element element = Element.forName(reader.getLocalName());
        switch (element) {
            case APPLICATION_NAME: {
                earMetaData.setApplicationName(getElementText(reader, propertyReplacer));
                break;
            }
            case INITIALIZATION_IN_ORDER: {
                earMetaData.setInitializeInOrder(Boolean.parseBoolean(getElementText(reader, propertyReplacer)));
                break;
            }
            case LIBRARY_DIRECTORY: {
                earMetaData.setLibraryDirectory(getElementText(reader, propertyReplacer));
                break;
            }
            case MESSAGE_DESTINATION: {
                earMetaData.getEarEnvironmentRefsGroup().getMessageDestinations().add(MessageDestinationMetaDataParser.parse(reader, propertyReplacer));
                break;
            }
            case MODULE: {
                earMetaData.getModules().add(EarModuleMetaDataParser.parse(reader, propertyReplacer));
                break;
            }
            case SECURITY_ROLE: {
                earMetaData.getSecurityRoles().add(SecurityRoleMetaDataParser.parse(reader, propertyReplacer));
                break;
            }
            default:
                throw unexpectedElement(reader);
        }
    }
}
