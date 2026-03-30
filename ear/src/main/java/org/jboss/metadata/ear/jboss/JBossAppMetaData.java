/*
 * Copyright The JBoss Metadata Authors
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.metadata.ear.jboss;


import org.jboss.metadata.ear.spec.EarMetaData;
import org.jboss.metadata.ear.spec.EarVersion;

/**
 * The jboss application metadata
 *
 * @author Scott.Stark@jboss.org
 * @version $Revision: 78586 $
 */
public class JBossAppMetaData extends EarMetaData {
    private static final long serialVersionUID = 3;
    /**
     * The default application security domain
     */
    private String securityDomain;
    /**
     * The unauthenticated principal
     */
    private String unauthenticatedPrincipal;

    /**
     * Distinct name for this application
     */
    private String distinctName;

    /**
     * Whether appclient processes should ignore non-"java" modules in this application.
     */
    private boolean limitAppclientModules;

    public JBossAppMetaData() {
        super(EarVersion.APP_10_0);
    }

    public JBossAppMetaData(EarVersion earVersion) {
        super(earVersion);
    }

    public String getSecurityDomain() {
        return securityDomain;
    }

    public void setSecurityDomain(String securityDomain) {
        this.securityDomain = securityDomain;
    }

    public String getUnauthenticatedPrincipal() {
        return unauthenticatedPrincipal;
    }

    public void setUnauthenticatedPrincipal(String unauthenticatedPrincipal) {
        this.unauthenticatedPrincipal = unauthenticatedPrincipal;
    }

    public boolean isLimitAppclientModules() {
        return limitAppclientModules;
    }

    public void setLimitAppclientModules(boolean limitAppclientModules) {
        this.limitAppclientModules = limitAppclientModules;
    }

    public void setDistinctName(final String distinctName) {
        this.distinctName = distinctName;
    }

    public String getDistinctName() {
        return this.distinctName;
    }
}
