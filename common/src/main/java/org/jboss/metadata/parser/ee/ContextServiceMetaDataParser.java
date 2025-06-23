/*
 * Copyright The JBoss Metadata Authors
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.metadata.parser.ee;

import org.jboss.metadata.javaee.spec.ContextServiceMetaData;
import org.jboss.metadata.javaee.spec.DescriptionsImpl;
import org.jboss.metadata.javaee.spec.PropertiesMetaData;
import org.jboss.metadata.parser.util.MetaDataElementParser;
import org.jboss.metadata.property.PropertyReplacer;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author emmartins
 */
public class ContextServiceMetaDataParser extends MetaDataElementParser {

    public static ContextServiceMetaData parse(XMLStreamReader reader, PropertyReplacer propertyReplacer)
            throws XMLStreamException {
        ContextServiceMetaData metaData = new ContextServiceMetaData();

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
                case CLEARED:
                    String cleared = getElementText(reader, propertyReplacer);
                    if (metaData.getCleared() == null) {
                        metaData.setCleared(new HashSet<>());
                    }
                    metaData.getCleared().add(cleared);
                    break;
                case PROPAGATED:
                    String propagated = getElementText(reader, propertyReplacer);
                    if (metaData.getPropagated() == null) {
                        metaData.setPropagated(new HashSet<>());
                    }
                    metaData.getPropagated().add(propagated);
                    break;
                case UNCHANGED:
                    String unchanged = getElementText(reader, propertyReplacer);
                    if (metaData.getUnchanged() == null) {
                        metaData.setUnchanged(new HashSet<>());
                    }
                    metaData.getUnchanged().add(unchanged);
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
                default:
                    throw unexpectedElement(reader);
            }
        }

        return metaData;
    }

}
