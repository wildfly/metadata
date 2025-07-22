/*
 * Copyright The JBoss Metadata Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.jboss.metadata.parser.jsp;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.jboss.metadata.javaee.spec.DescriptionGroupMetaData;
import org.jboss.metadata.parser.ee.DescriptionGroupMetaDataParser;
import org.jboss.metadata.parser.util.MetaDataElementParser;
import org.jboss.metadata.web.spec.FunctionMetaData;
import org.jboss.metadata.web.spec.ListenerMetaData;
import org.jboss.metadata.web.spec.TagFileMetaData;
import org.jboss.metadata.web.spec.TagMetaData;
import org.jboss.metadata.web.spec.TldExtensionMetaData;
import org.jboss.metadata.web.spec.TldMetaData;


/**
 * @author Remy Maucherat
 */
public class TldMetaDataParser extends MetaDataElementParser {

    public static TldMetaData parse(XMLStreamReader reader) throws XMLStreamException {

        reader.require(START_DOCUMENT, null, null);
        // Read until the first start element
        Version version = null;
        while (reader.hasNext() && reader.next() != START_ELEMENT) {
            if (reader.getEventType() == DTD) {
                String dtdLocation = readDTDLocation(reader);
                if (dtdLocation != null) {
                    version = Location.getVersion(dtdLocation);
                }
                if (version == null) {
                    // DTD->getText() is incomplete and not parsable with Xerces from Sun JDK 6,
                    // so assume TLD 1.2
                    version = Version.TLD_1_2;
                }
            }
        }
        String schemaLocation = readSchemaLocation(reader);
        if (schemaLocation != null) {
            version = Location.getVersion(schemaLocation);
        }
        if (version == null) {
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
            if ("2.0".equals(versionString)) {
                version = Version.TLD_2_0;
            } else if ("2.1".equals(versionString)) {
                version = Version.TLD_2_1;
            } else if ("3.0".equals(versionString)) {
                version = Version.TLD_3_0;
            } else if ("3.1".equals(versionString)) {
                version = Version.TLD_3_1;
            } else if ("4.0".equals(versionString)) {
                version = Version.TLD_4_0;
            }
        }
        if (version == null) {
            // It is likely an error to not have a version at this point though
            version = Version.TLD_4_0;
        }
        TldMetaData tld = new TldMetaData();
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
                    tld.setId(value);
                    break;
                }
                case VERSION: {
                    tld.setVersion(value);
                    break;
                }
                default:
                    throw unexpectedAttribute(reader, i);
            }
        }

        DescriptionGroupMetaData descriptionGroup = new DescriptionGroupMetaData();
        // Handle elements
        while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            if (DescriptionGroupMetaDataParser.parse(reader, descriptionGroup)) {
                if (tld.getDescriptionGroup() == null) {
                    tld.setDescriptionGroup(descriptionGroup);
                }
                continue;
            }
            final Element element = Element.forName(reader.getLocalName());
            switch (element) {
                case TLIB_VERSION:
                    tld.setTlibVersion(getElementText(reader));
                    break;
                case TLIBVERSION:
                    if (version == Version.TLD_1_1) {
                        tld.setTlibVersion(getElementText(reader));
                    } else if (version == Version.TLD_1_2) {
                        // Allow invalid legacy element
                        tld.setTlibVersion(getElementText(reader));
                    } else {
                        throw unexpectedElement(reader);
                    }
                    break;
                case SHORT_NAME:
                    tld.setShortName(getElementText(reader));
                    break;
                case SHORTNAME:
                    if (version == Version.TLD_1_1) {
                        tld.setShortName(getElementText(reader));
                    } else if (version == Version.TLD_1_2) {
                        // Allow invalid legacy element
                        tld.setShortName(getElementText(reader));
                    } else {
                        throw unexpectedElement(reader);
                    }
                    break;
                case JSPVERSION:
                    if (version == Version.TLD_1_1) {
                        tld.setJspVersion(getElementText(reader));
                    } else if (version == Version.TLD_1_2) {
                        // Allow invalid legacy element
                        tld.setJspVersion(getElementText(reader));
                    } else {
                        throw unexpectedElement(reader);
                    }
                    break;
                case JSP_VERSION:
                    if (version == Version.TLD_1_2) {
                        tld.setJspVersion(getElementText(reader));
                    } else if (version != Version.TLD_1_1) {
                        // This is not valid, but still being used in 2.0+ TLDs
                        tld.setJspVersion(getElementText(reader));
                    } else {
                        throw unexpectedElement(reader);
                    }
                    break;
                case INFO:
                    if (version == Version.TLD_1_1) {
                        getElementText(reader);//ignore
                    } else if (version == Version.TLD_1_2) {
                        // Ignore invalid legacy element
                        getElementText(reader);
                    } else {
                        throw unexpectedElement(reader);
                    }
                    break;
                case SMALL_ICON:
                    if (version == Version.TLD_1_2) {
                        getElementText(reader);//ignore
                    } else {
                        throw unexpectedElement(reader);
                    }
                    break;
                case LARGE_ICON:
                    if (version == Version.TLD_1_2) {
                        getElementText(reader);//ignore
                    } else {
                        throw unexpectedElement(reader);
                    }
                    break;
                case URI:
                    tld.setUri(getElementText(reader));
                    break;
                case VALIDATOR:
                    tld.setValidator(ValidatorMetaDataParser.parse(reader));
                    break;
                case LISTENER:
                    List<ListenerMetaData> listeners = tld.getListeners();
                    if (listeners == null) {
                        listeners = new ArrayList<ListenerMetaData>();
                        tld.setListeners(listeners);
                    }
                    listeners.add(ListenerMetaDataParser.parse(reader));
                    break;
                case TAG:
                    List<TagMetaData> tags = tld.getTags();
                    if (tags == null) {
                        tags = new ArrayList<TagMetaData>();
                        tld.setTags(tags);
                    }
                    tags.add(TagMetaDataParser.parse(reader, version));
                    break;
                case TAG_FILE:
                    List<TagFileMetaData> tagfiles = tld.getTagFiles();
                    if (tagfiles == null) {
                        tagfiles = new ArrayList<TagFileMetaData>();
                        tld.setTagFiles(tagfiles);
                    }
                    tagfiles.add(TagFileMetaDataParser.parse(reader));
                    break;
                case FUNCTION:
                    List<FunctionMetaData> functions = tld.getFunctions();
                    if (functions == null) {
                        functions = new ArrayList<FunctionMetaData>();
                        tld.setFunctions(functions);
                    }
                    functions.add(FunctionMetaDataParser.parse(reader));
                    break;
                case TAGLIB_EXTENSION:
                    List<TldExtensionMetaData> extensions = tld.getTaglibExtensions();
                    if (extensions == null) {
                        extensions = new ArrayList<TldExtensionMetaData>();
                        tld.setTaglibExtensions(extensions);
                    }
                    extensions.add(TldExtensionMetaDataParser.parse(reader));
                    break;
                default:
                    throw unexpectedElement(reader);
            }
        }

        return tld;
    }

}
