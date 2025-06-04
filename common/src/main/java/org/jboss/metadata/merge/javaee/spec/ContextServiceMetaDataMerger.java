/*
 * Copyright The JBoss Metadata Authors
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.metadata.merge.javaee.spec;

import org.jboss.metadata.javaee.spec.ContextServiceMetaData;
import org.jboss.metadata.merge.javaee.support.NamedMetaDataMerger;

import java.util.HashSet;

/**
 *
 * @author emmartins
 *
 */
public class ContextServiceMetaDataMerger {

    public static ContextServiceMetaData merge(ContextServiceMetaData dest, ContextServiceMetaData original) {
        ContextServiceMetaData merged = new ContextServiceMetaData();
        merge(merged, dest, original);
        return merged;
    }

    /**
     * Merge the contents of override with original into this.
     *
     * @param override data which overrides original
     * @param original the original data
     */
    public static void merge(ContextServiceMetaData dest, ContextServiceMetaData override, ContextServiceMetaData original) {
        NamedMetaDataMerger.merge(dest, override, original);

        if (override != null && override.getCleared() != null)
            dest.setCleared(new HashSet<>(override.getCleared()));
        else if (original != null && original.getCleared() != null)
            dest.getCleared().addAll(original.getCleared());

        if (override != null && override.getPropagated() != null)
            dest.setPropagated(new HashSet<>(override.getPropagated()));
        else if (original != null && original.getPropagated() != null)
            dest.getPropagated().addAll(original.getPropagated());

        if (override != null && override.getUnchanged() != null)
            dest.setUnchanged(new HashSet<>(override.getUnchanged()));
        else if (original != null && original.getUnchanged() != null)
            dest.getUnchanged().addAll(original.getUnchanged());

        if (override != null && override.getProperties() != null)
            dest.setProperties(override.getProperties());
        else if (original != null && original.getProperties() != null)
            dest.setProperties(original.getProperties());

        if (override != null && override.getQualifier() != null)
            dest.setQualifier(override.getQualifier());
        else if (original != null && original.getQualifier() != null)
            dest.setQualifier(original.getQualifier());
    }
}
