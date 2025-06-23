/*
 * Copyright The JBoss Metadata Authors
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.metadata.merge.javaee.spec;

import org.jboss.metadata.javaee.spec.ManagedThreadFactoryMetaData;
import org.jboss.metadata.merge.javaee.support.NamedMetaDataMerger;

/**
 *
 * @author emmartins
 *
 */
public class ManagedThreadFactoryMetaDataMerger {

    public static ManagedThreadFactoryMetaData merge(ManagedThreadFactoryMetaData dest, ManagedThreadFactoryMetaData original) {
        ManagedThreadFactoryMetaData merged = new ManagedThreadFactoryMetaData();
        merge(merged, dest, original);
        return merged;
    }

    /**
     * Merge the contents of override with original into this.
     *
     * @param override data which overrides original
     * @param original the original data
     */
    public static void merge(ManagedThreadFactoryMetaData dest, ManagedThreadFactoryMetaData override, ManagedThreadFactoryMetaData original) {

        NamedMetaDataMerger.merge(dest, override, original);

        if (override != null && override.getContextServiceRef() != null)
            dest.setContextServiceRef(override.getContextServiceRef());
        else if (original != null && original.getContextServiceRef() != null)
            dest.setContextServiceRef(original.getContextServiceRef());

        if (override != null)
            dest.setPriority(override.getPriority());
        else if (original != null)
            dest.setPriority(original.getPriority());

        if (override != null && override.getProperties() != null)
            dest.setProperties(override.getProperties());
        else if (original != null && original.getProperties() != null)
            dest.setProperties(original.getProperties());

        if (override != null && override.getQualifier() != null)
            dest.setQualifier(override.getQualifier());
        else if (original != null && original.getQualifier() != null)
            dest.setQualifier(original.getQualifier());

        if (override != null && override.getVirtual() != null)
            dest.setVirtual(override.getVirtual());
        else if (original != null && original.getVirtual() != null)
            dest.setVirtual(original.getVirtual());
    }

}
