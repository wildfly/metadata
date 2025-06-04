/*
 * Copyright The JBoss Metadata Authors
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.metadata.merge.javaee.spec;

import org.jboss.metadata.javaee.spec.ManagedExecutorMetaData;
import org.jboss.metadata.merge.javaee.support.NamedMetaDataMerger;

/**
 *
 * @author emmartins
 *
 */
public class ManagedExecutorMetaDataMerger {

    public static ManagedExecutorMetaData merge(ManagedExecutorMetaData dest, ManagedExecutorMetaData original) {
        ManagedExecutorMetaData merged = new ManagedExecutorMetaData();
        merge(merged, dest, original);
        return merged;
    }

    /**
     * Merge the contents of override with original into this.
     *
     * @param override data which overrides original
     * @param original the original data
     */
    public static void merge(ManagedExecutorMetaData dest, ManagedExecutorMetaData override, ManagedExecutorMetaData original) {

        NamedMetaDataMerger.merge(dest, override, original);

        if (override != null && override.getContextServiceRef() != null)
            dest.setContextServiceRef(override.getContextServiceRef());
        else if (original != null && original.getContextServiceRef() != null)
            dest.setContextServiceRef(original.getContextServiceRef());

        if (override != null && override.getHungTaskThreshold() != null)
            dest.setHungTaskThreshold(override.getHungTaskThreshold());
        else if (original != null && original.getHungTaskThreshold() != null)
            dest.setHungTaskThreshold(original.getHungTaskThreshold());

        if (override != null && override.getMaxAsync() != null)
            dest.setMaxAsync(override.getMaxAsync());
        else if (original != null && original.getMaxAsync() != null)
            dest.setMaxAsync(original.getMaxAsync());

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
