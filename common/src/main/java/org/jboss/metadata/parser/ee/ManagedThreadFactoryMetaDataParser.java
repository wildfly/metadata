/*
 * Copyright The JBoss Metadata Authors
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.metadata.parser.ee;

import org.jboss.metadata.javaee.spec.DescriptionsImpl;
import org.jboss.metadata.javaee.spec.ManagedThreadFactoryMetaData;
import org.jboss.metadata.javaee.spec.PropertiesMetaData;
import org.jboss.metadata.parser.util.MetaDataElementParser;
import org.jboss.metadata.property.PropertyReplacer;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author emmartins
 */
public class ManagedThreadFactoryMetaDataParser extends MetaDataElementParser {

    public static ManagedThreadFactoryMetaData parse(XMLStreamReader reader, PropertyReplacer propertyReplacer)
            throws XMLStreamException {
        ManagedThreadFactoryMetaData metaData = new ManagedThreadFactoryMetaData();

        IdMetaDataParser.parseAttributes(reader, metaData);

        DescriptionsImpl descriptions = new DescriptionsImpl();
        // Handle elements
        while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            if (DescriptionsMetaDataParser.parse(reader, descriptions, propertyReplacer)) {
                if (metaData.getDescriptions() == null) {
                    metaData.setDescriptions(descriptions);
                }
                continue;
            }
            final Element element = Element.forName(reader.getLocalName());
            switch (element) {
                case NAME:
                    metaData.setName(getElementText(reader, propertyReplacer));
                    break;
                case CONTEXT_SERVICE_REF:
                    metaData.setContextServiceRef(getElementText(reader, propertyReplacer));
                    break;
                case PRIORITY:
                    metaData.setPriority(Integer.valueOf(getElementText(reader, propertyReplacer)));
                    break;
                case PROPERTY:
                    PropertiesMetaData properties = metaData.getProperties();
                    if (properties == null) {
                        properties = new PropertiesMetaData();
                        metaData.setProperties(properties);
                    }
                    properties.add(PropertyMetaDataParser.parse(reader, propertyReplacer));
                    break;
                case QUALIFIER:
                    List<String> qualifier = metaData.getQualifier();
                    if (qualifier == null) {
                        qualifier = new ArrayList<>();
                        metaData.setQualifier(qualifier);
                    }
                    qualifier.add(getElementText(reader, propertyReplacer));
                    break;
                case VIRTUAL:
                    metaData.setVirtual(Boolean.valueOf(getElementText(reader, propertyReplacer)));
                    break;
                default:
                    throw unexpectedElement(reader);
            }
        }

        return metaData;
    }

}
