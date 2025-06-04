/*
 * Copyright The JBoss Metadata Authors
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.test.metadata.javaee;

import java.util.List;
import java.util.Set;

import org.jboss.annotation.javaee.Description;
import org.jboss.annotation.javaee.Descriptions;
import org.jboss.annotation.javaee.DisplayName;
import org.jboss.annotation.javaee.DisplayNames;
import org.jboss.annotation.javaee.Icon;
import org.jboss.annotation.javaee.Icons;
import org.jboss.metadata.javaee.jboss.AnnotationMetaData;
import org.jboss.metadata.javaee.jboss.AnnotationPropertiesMetaData;
import org.jboss.metadata.javaee.jboss.AnnotationPropertyMetaData;
import org.jboss.metadata.javaee.jboss.AnnotationsMetaData;
import org.jboss.metadata.javaee.jboss.JndiRefMetaData;
import org.jboss.metadata.javaee.jboss.JndiRefsMetaData;
import org.jboss.metadata.javaee.spec.AdministeredObjectMetaData;
import org.jboss.metadata.javaee.spec.AdministeredObjectsMetaData;
import org.jboss.metadata.javaee.spec.ConnectionFactoriesMetaData;
import org.jboss.metadata.javaee.spec.ConnectionFactoryMetaData;
import org.jboss.metadata.javaee.spec.ContextServiceMetaData;
import org.jboss.metadata.javaee.spec.ContextServicesMetaData;
import org.jboss.metadata.javaee.spec.DataSourceMetaData;
import org.jboss.metadata.javaee.spec.DataSourcesMetaData;
import org.jboss.metadata.javaee.spec.DescriptionGroupMetaData;
import org.jboss.metadata.javaee.spec.DescriptionsImpl;
import org.jboss.metadata.javaee.spec.DisplayNamesImpl;
import org.jboss.metadata.javaee.spec.EJBLocalReferenceMetaData;
import org.jboss.metadata.javaee.spec.EJBLocalReferencesMetaData;
import org.jboss.metadata.javaee.spec.EJBReferenceMetaData;
import org.jboss.metadata.javaee.spec.EJBReferenceType;
import org.jboss.metadata.javaee.spec.EJBReferencesMetaData;
import org.jboss.metadata.javaee.spec.EmptyMetaData;
import org.jboss.metadata.javaee.spec.Environment;
import org.jboss.metadata.javaee.spec.EnvironmentEntriesMetaData;
import org.jboss.metadata.javaee.spec.EnvironmentEntryMetaData;
import org.jboss.metadata.javaee.spec.IconsImpl;
import org.jboss.metadata.javaee.spec.IsolationLevelType;
import org.jboss.metadata.javaee.spec.JMSConnectionFactoriesMetaData;
import org.jboss.metadata.javaee.spec.JMSConnectionFactoryMetaData;
import org.jboss.metadata.javaee.spec.JMSDestinationMetaData;
import org.jboss.metadata.javaee.spec.JMSDestinationsMetaData;
import org.jboss.metadata.javaee.spec.LifecycleCallbackMetaData;
import org.jboss.metadata.javaee.spec.LifecycleCallbacksMetaData;
import org.jboss.metadata.javaee.spec.MailSessionMetaData;
import org.jboss.metadata.javaee.spec.MailSessionsMetaData;
import org.jboss.metadata.javaee.spec.ManagedExecutorMetaData;
import org.jboss.metadata.javaee.spec.ManagedExecutorsMetaData;
import org.jboss.metadata.javaee.spec.ManagedScheduledExecutorsMetaData;
import org.jboss.metadata.javaee.spec.ManagedThreadFactoriesMetaData;
import org.jboss.metadata.javaee.spec.ManagedThreadFactoryMetaData;
import org.jboss.metadata.javaee.spec.MessageDestinationMetaData;
import org.jboss.metadata.javaee.spec.MessageDestinationReferenceMetaData;
import org.jboss.metadata.javaee.spec.MessageDestinationReferencesMetaData;
import org.jboss.metadata.javaee.spec.MessageDestinationUsageType;
import org.jboss.metadata.javaee.spec.MessageDestinationsMetaData;
import org.jboss.metadata.javaee.spec.PersistenceContextReferenceMetaData;
import org.jboss.metadata.javaee.spec.PersistenceContextReferencesMetaData;
import org.jboss.metadata.javaee.spec.PersistenceContextSynchronizationType;
import org.jboss.metadata.javaee.spec.PersistenceContextTypeDescription;
import org.jboss.metadata.javaee.spec.PersistenceUnitReferenceMetaData;
import org.jboss.metadata.javaee.spec.PersistenceUnitReferencesMetaData;
import org.jboss.metadata.javaee.spec.PropertiesMetaData;
import org.jboss.metadata.javaee.spec.PropertyMetaData;
import org.jboss.metadata.javaee.spec.RemoteEnvironment;
import org.jboss.metadata.javaee.spec.ResourceAuthorityType;
import org.jboss.metadata.javaee.spec.ResourceEnvironmentReferenceMetaData;
import org.jboss.metadata.javaee.spec.ResourceEnvironmentReferencesMetaData;
import org.jboss.metadata.javaee.spec.ResourceInjectionMetaData;
import org.jboss.metadata.javaee.spec.ResourceInjectionTargetMetaData;
import org.jboss.metadata.javaee.spec.ResourceReferenceMetaData;
import org.jboss.metadata.javaee.spec.ResourceReferencesMetaData;
import org.jboss.metadata.javaee.spec.ResourceSharingScopeType;
import org.jboss.metadata.javaee.spec.SecurityRoleMetaData;
import org.jboss.metadata.javaee.spec.SecurityRolesMetaData;
import org.jboss.metadata.javaee.spec.ServiceReferenceMetaData;
import org.jboss.metadata.javaee.spec.ServiceReferencesMetaData;
import org.jboss.metadata.javaee.spec.TransactionSupportType;
import org.jboss.metadata.javaee.support.IdMetaData;
import org.jboss.metadata.merge.javaee.spec.JavaEEVersion;

/**
 * AbstractJavaEEEverythingTest.
 *
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractJavaEEEverythingTest extends AbstractJavaEEMetaDataTest {
    /**
     * Define two testing modes for assertions:
     * - JBOSS: we're testing a JBoss view (jboss_5_0.xsd)
     * - JBOSS_DTD: we're testing jboss_5_0.dtd view
     * - SPEC: we're testing a spec view
     */
    public enum Mode {
        JBOSS,
        JBOSS_DTD,
        SPEC,
        JAKARTAEE
    }

    public enum Descriptor {
        UNKNOWN,
        APPLICATION_CLIENT,
        APPLICATION,
        EJB,
        WEB
    }

    protected AbstractJavaEEEverythingTest() {
        super();
    }

    protected AbstractJavaEEEverythingTest(String name) {
        super(name);
    }

    protected void assertId(String prefix, Object object) {
        assertNotNull(object);
        assertTrue(object.getClass() + " is not an IdMetaData", object instanceof IdMetaData);
        IdMetaData idMetaData = (IdMetaData) object;
        assertEquals(prefix + "-id", idMetaData.getId());
    }

    protected void assertDescriptionGroup(String suffix, DescriptionGroupMetaData group) {
        assertNotNull("no description group meta data is set", group);
        assertDescriptions(suffix, group.getDescriptions());
        assertDisplayNames(suffix, group.getDisplayNames());
        assertIcons(suffix, group.getIcons());
    }

    protected void assertDescriptions(String suffix, Descriptions descriptions) {
        assertNotNull("no descriptions are set", descriptions);
        assertTrue(descriptions instanceof DescriptionsImpl);
        DescriptionsImpl impl = (DescriptionsImpl) descriptions;
        assertEquals(3, impl.size());
        assertDescription("en", suffix, impl);
        assertDescription("fr", suffix, impl);
        assertDescription("de", suffix, impl);
    }

    protected void assertDescription(String lang, String suffix, DescriptionsImpl impl) {
        Description description = impl.get(lang);
        assertNotNull(description);
        assertEquals(lang + "-" + suffix + "-desc", description.value());
    }

    protected void assertDisplayNames(String suffix, DisplayNames displayNames) {
        assertNotNull(displayNames);
        assertTrue(displayNames instanceof DisplayNamesImpl);
        DisplayNamesImpl impl = (DisplayNamesImpl) displayNames;
        assertEquals(3, impl.size());
        assertDisplayName("en", suffix, impl);
        assertDisplayName("fr", suffix, impl);
        assertDisplayName("de", suffix, impl);
    }

    protected void assertDisplayName(String lang, String suffix, DisplayNamesImpl impl) {
        DisplayName displayName = impl.get(lang);
        assertNotNull(displayName);
        assertEquals(lang + "-" + suffix + "-disp", displayName.value());
    }

    protected void assertIcons(String suffix, Icons icons) {
        assertNotNull("no icons are set", icons);
        assertTrue(icons instanceof IconsImpl);
        IconsImpl impl = (IconsImpl) icons;
        assertEquals(3, impl.size());
        assertIcon("en", suffix, impl);
        assertIcon("fr", suffix, impl);
        assertIcon("de", suffix, impl);
    }

    protected void assertIcon(String lang, String suffix, IconsImpl impl) {
        Icon icon = impl.get(lang);
        assertNotNull(icon);
        assertEquals(lang + "-" + suffix + "-small-icon", icon.smallIcon());
        assertEquals(lang + "-" + suffix + "-large-icon", icon.largeIcon());
        assertId(lang + "-" + suffix + "-icon", icon);
    }

    protected void assertClass(String prefix, String type, String className) {
        assertEquals(prefix + type, className);
    }

    protected void assertEmpty(String prefix, String type, EmptyMetaData emptyMetaData) {
        assertNotNull(emptyMetaData);
        assertId(prefix + type, emptyMetaData);
    }

    protected void assertSecurityRoles(int size, SecurityRolesMetaData securityRolesMetaData, Mode mode) {
        assertNotNull("security roles is null", securityRolesMetaData);
        assertEquals(securityRolesMetaData.getId(), size, securityRolesMetaData.size());
        int count = 1;
        for (SecurityRoleMetaData securityRoleMetaData : securityRolesMetaData) {
            assertSecurityRole("securityRole", count, securityRoleMetaData, mode);
            ++count;
        }
    }

    protected void assertSecurityRole(String prefix, int count, SecurityRoleMetaData securityRoleMetaData, Mode mode) {
        assertNotNull(securityRoleMetaData);
        if (mode != Mode.JBOSS_DTD) {
            assertId(prefix + count, securityRoleMetaData);
            assertDescriptions(prefix + count, securityRoleMetaData.getDescriptions());
        }
        assertEquals("securityRoleRef" + count + "RoleLink", securityRoleMetaData.getRoleName());
    }

    /**
     * @param prefix
     * @param size
     * @param principals
     */
    protected void assertPrincipals(String prefix, int count, int size, Set<String> principals) {
        assertNotNull(principals);
        assertEquals(size, principals.size());
        for (int n = 1; n <= principals.size(); ++n) {
            String principal = prefix + count + "Principal" + n;
            assertTrue(principal, principals.contains(principal));
        }
    }

   /* TODO
   protected void assertSecurityRoles(int size, Map<String, org.jboss.security.SecurityRoleMetaData> securityRoles)
   {
      assertNotNull(securityRoles);
      assertEquals(size, securityRoles.size());
      int count = 1;
      for (org.jboss.security.SecurityRoleMetaData securityRoleMetaData : securityRoles.values())
      {
         assertSecurityRole("securityRoleRef" + count, securityRoleMetaData);
         ++count;
      }
   }

   protected void assertSecurityRole(String prefix, org.jboss.security.SecurityRoleMetaData securityRole)
   {
      assertNotNull(securityRole);
      assertEquals(prefix + "RoleLink", securityRole.getRoleName());
   }
   */

    protected void assertMessageDestinations(int size, MessageDestinationsMetaData messageDestinationsMetaData, Mode mode) {
        assertMessageDestinations(size, messageDestinationsMetaData, mode, JavaEEVersion.UNKNOWN);
    }

    protected void assertMessageDestinations(int size, MessageDestinationsMetaData messageDestinationsMetaData, Mode mode, JavaEEVersion version) {
        assertNotNull("message destinations is null", messageDestinationsMetaData);
        assertEquals(size, messageDestinationsMetaData.size());
        int count = 1;
        for (MessageDestinationMetaData messageDestinationMetaData : messageDestinationsMetaData) {
            if(version == JavaEEVersion.V6 || version == JavaEEVersion.V7) {
                assertMessageDestination60("messageDestination" + count, messageDestinationMetaData, mode);
            } else {
                assertMessageDestination("messageDestination" + count, messageDestinationMetaData, mode);
            }
            ++count;
        }
    }

    protected void assertMessageDestination(String prefix, MessageDestinationMetaData messageDestinationMetaData, Mode mode) {
        assertMessageDestination14(prefix, messageDestinationMetaData, mode);
    }

    protected void assertMessageDestination14(String prefix, MessageDestinationMetaData messageDestinationMetaData, Mode mode) {
        assertNotNull(messageDestinationMetaData);
        if (mode != Mode.JBOSS_DTD) {
            assertId(prefix, messageDestinationMetaData);
            assertDescriptionGroup(prefix, messageDestinationMetaData.getDescriptionGroup());
        }
        assertEquals(prefix + "Name", messageDestinationMetaData.getMessageDestinationName());
    }

    protected void assertMessageDestination50(String prefix, MessageDestinationMetaData messageDestinationMetaData, Mode mode) {
        assertMessageDestination14(prefix, messageDestinationMetaData, mode);
        assertEquals(prefix + "MappedName", messageDestinationMetaData.getMappedName());
    }

    protected void assertMessageDestination60(String prefix, MessageDestinationMetaData messageDestinationMetaData, Mode mode) {
        assertMessageDestination50(prefix, messageDestinationMetaData, mode);
        assertEquals(prefix + "LookupName", messageDestinationMetaData.getLookupName());
    }

    protected void assertLifecycleCallbacks(String ejbName, String type, int size, LifecycleCallbacksMetaData lifecycleCallbacksMetaData) {
        assertNotNull("no " + type + " lifecycle callbacks are set", lifecycleCallbacksMetaData);
        assertEquals(size, lifecycleCallbacksMetaData.size());
        int count = 1;
        for (LifecycleCallbackMetaData lifecycleCallback : lifecycleCallbacksMetaData) {
            assertEquals(ejbName + type + count + "Class", lifecycleCallback.getClassName());
            assertEquals(ejbName + type + count + "Method", lifecycleCallback.getMethodName());
            ++count;
        }
    }

    /**
     * Use the version instead. This is method was kept to provide retro compatibility, thus it should not be used for testing {@link Environment} v7+
     * @param prefix
     * @param environment
     * @param full
     * @param mode
     */
    @Deprecated
    protected void assertEnvironment(String prefix, Environment environment, boolean full, Mode mode) {
        assertEnvironment(prefix, environment, full, Descriptor.UNKNOWN, mode, JavaEEVersion.V6);
    }

    /**
     * Asserts expected {@link Environment}.
     * @param prefix
     * @param environment
     * @param full
     * @param descriptor
     * @param mode
     * @param version
     */
    protected void assertEnvironment(String prefix, Environment environment, boolean full, Descriptor descriptor, Mode mode, JavaEEVersion version) {
        assertRemoteEnvironment(prefix, environment, full, descriptor, mode, version);
        assertEjbLocalRefs(prefix, 2, environment.getEjbLocalReferences(), full, mode);
        if (full) {
            assertPersistenceContextRefs(prefix, 2, environment.getPersistenceContextRefs(), mode, version);
        }
        if(Descriptor.APPLICATION == descriptor) {
            assertContextServices(prefix, environment.getContextServices(), mode, full, version);
            assertManagedExecutors(prefix, environment.getManagedExecutors(), mode, full, version);
            assertManagedScheduledExecutors(prefix, environment.getManagedScheduledExecutors(), mode, full, version);
            assertManagedThreadFactories(prefix, environment.getManagedThreadFactories(), mode, full, version);
        }
    }

    protected void assertNullEnvironment(Environment environment) {
        if (environment != null) {
            assertNull(environment.getEnvironmentEntries());
            assertNull(environment.getEjbReferences());
            assertNull(environment.getEjbLocalReferences());
            assertNull(environment.getServiceReferences());
            assertNull(environment.getResourceReferences());
            assertNull(environment.getResourceEnvironmentReferences());
            assertNull(environment.getMessageDestinationReferences());
            assertNull(environment.getPersistenceContextRefs());
            assertNull(environment.getPersistenceUnitRefs());
            assertNull(environment.getPostConstructs());
            assertNull(environment.getPreDestroys());
            assertNull(environment.getDataSources());
            assertNull(environment.getAdministeredObjects());
            assertNull(environment.getConnectionFactories());
            assertNull(environment.getJmsConnectionFactories());
            assertNull(environment.getJmsDestinations());
            assertNull(environment.getMailSessions());
            assertNull(environment.getContextServices());
            assertNull(environment.getManagedExecutors());
            assertNull(environment.getManagedScheduledExecutors());
            assertNull(environment.getManagedThreadFactories());
        }
    }

    protected void assertEnvEntries(String prefix, int size, EnvironmentEntriesMetaData environmentEntriesMetaData, boolean full, Mode mode) {
        assertNotNull(environmentEntriesMetaData);
        assertEquals(size, environmentEntriesMetaData.size());
        int count = 1;
        for (EnvironmentEntryMetaData environmentEntryMetaData : environmentEntriesMetaData) {
            assertId(prefix + "EnvEntry" + count, environmentEntryMetaData);
            assertDescriptions(prefix + "EnvEntry" + count, environmentEntryMetaData.getDescriptions());
            assertEquals(prefix + "EnvEntry" + count + "Name", environmentEntryMetaData.getEnvEntryName());
            assertEquals("java.lang.String", environmentEntryMetaData.getType());
            assertEquals(prefix + "EnvEntry" + count + "Value", environmentEntryMetaData.getValue());
            assertResourceGroup(prefix + "EnvEntry" + count, environmentEntryMetaData, full, count == 1, mode);
            ++count;
        }
    }

    @Deprecated
    protected void assertRemoteEnvironment(String prefix, RemoteEnvironment environment, boolean full, Mode mode) {
        assertRemoteEnvironment(prefix, environment, full, Descriptor.UNKNOWN, mode, JavaEEVersion.UNKNOWN);
    }

    protected void assertRemoteEnvironment(String prefix, RemoteEnvironment environment, boolean full, Descriptor descriptor, Mode mode, JavaEEVersion version) {
        assertNotNull(environment);
        if (full) { assertEnvEntries(prefix, 2, environment.getEnvironmentEntries(), full, mode); }
        assertEjbRefs(prefix, 2, environment.getEjbReferences(), full, mode);
        assertServiceRefs(prefix, 2, environment.getServiceReferences(), full);
        assertResourceRefs(prefix, 2, environment.getResourceReferences(), full, mode);
        assertResourceEnvRefs(prefix, 2, environment.getResourceEnvironmentReferences(), full, mode);
        assertMessageDestinationRefs(prefix, 3, environment.getMessageDestinationReferences(), full, mode);
        if (full) {
            assertPersistenceUnitRefs(prefix, 2, environment.getPersistenceUnitRefs(), mode);
            if(descriptor != Descriptor.APPLICATION) {
                assertLifecycleCallbacks(prefix, "PostConstruct", 2, environment.getPostConstructs());
                assertLifecycleCallbacks(prefix, "PreDestroy", 2, environment.getPreDestroys());
            }
        }
        assertDataSources(prefix, environment.getDataSources(), mode, full, version);
        assertAdministeredObjects(prefix, environment.getAdministeredObjects(), mode, full, version);
        assertConnectionFactories(prefix, environment.getConnectionFactories(), mode, full, version);
        assertJMSConnectionFactories(prefix, environment.getJmsConnectionFactories(), mode, full, version);
        assertJMSDestinations(prefix, environment.getJmsDestinations(), mode, full, version);
        assertMailSessions(prefix, environment.getMailSessions(), mode, full, version);
    }

    protected void assertEjbRefs(String prefix, int size, EJBReferencesMetaData ejbReferencesMetaData, boolean full, Mode mode) {
        assertNotNull("no ejb-refs are set", ejbReferencesMetaData);
        assertEquals(size, ejbReferencesMetaData.size());
        int count = 1;
        for (EJBReferenceMetaData ejbReferenceMetaData : ejbReferencesMetaData) {
            if (mode != Mode.JBOSS_DTD) {
                assertId(prefix + "EjbRef" + count, ejbReferenceMetaData);
                assertDescriptions(prefix + "EjbRef" + count, ejbReferenceMetaData.getDescriptions());
            }
            assertEquals(prefix + "EjbRef" + count + "Name", ejbReferenceMetaData.getEjbRefName());
            if (full) {
                if (count == 1) { assertEquals(EJBReferenceType.Session, ejbReferenceMetaData.getEjbRefType()); } else {
                    assertEquals(EJBReferenceType.Entity, ejbReferenceMetaData.getEjbRefType());
                }
                assertEquals(prefix + "EjbRef" + count + "Home", ejbReferenceMetaData.getHome());
                assertEquals(prefix + "EjbRef" + count + "Remote", ejbReferenceMetaData.getRemote());
                assertEquals(prefix + "EjbRef" + count + "EjbLink", ejbReferenceMetaData.getLink());
            }
            assertResourceGroup(prefix + "EjbRef" + count, ejbReferenceMetaData, full, count == 1, mode);
            ++count;
        }
    }

    protected void assertEjbLocalRefs(String prefix, int size, EJBLocalReferencesMetaData ejbReferencesMetaData, boolean full, Mode mode) {
        assertNotNull("no ejb local refs are set", ejbReferencesMetaData);
        assertEquals(size, ejbReferencesMetaData.size());
        int count = 1;
        for (EJBLocalReferenceMetaData ejbReferenceMetaData : ejbReferencesMetaData) {
            if (mode != Mode.JBOSS_DTD) {
                assertId(prefix + "EjbLocalRef" + count, ejbReferenceMetaData);
                assertDescriptions(prefix + "EjbLocalRef" + count, ejbReferenceMetaData.getDescriptions());
            }
            assertEquals(prefix + "EjbLocalRef" + count + "Name", ejbReferenceMetaData.getEjbRefName());
            if (full) {
                if (count == 1) { assertEquals(EJBReferenceType.Session, ejbReferenceMetaData.getEjbRefType()); } else {
                    assertEquals(EJBReferenceType.Entity, ejbReferenceMetaData.getEjbRefType());
                }
                assertEquals(prefix + "EjbLocalRef" + count + "LocalHome", ejbReferenceMetaData.getLocalHome());
                assertEquals(prefix + "EjbLocalRef" + count + "Local", ejbReferenceMetaData.getLocal());
                assertEquals(prefix + "EjbLocalRef" + count + "EjbLink", ejbReferenceMetaData.getLink());
            }
            assertResourceGroup(prefix + "EjbLocalRef" + count, ejbReferenceMetaData, full, count == 1, mode);
            ++count;
        }
    }

    protected void assertServiceRefs(String prefix, int size, ServiceReferencesMetaData serviceReferencesMetaData, boolean full) {
        if (full == false) { return; }

        assertNotNull("no service refs are set for: " + prefix, serviceReferencesMetaData);
        assertEquals(size, serviceReferencesMetaData.size());
        int count = 1;
        for (ServiceReferenceMetaData ref : serviceReferencesMetaData) {
            assertId(prefix + "ServiceRef" + count, ref);
            assertDescriptions(prefix + "ServiceRef" + count, ref.getDescriptionGroup().getDescriptions());
            assertEquals(prefix + "ServiceRef" + count + "Name", ref.getServiceRefName());
            assertEquals(prefix + "ServiceRef" + count + "Iface", ref.getServiceInterface());
            // TODO: full ServiceReferenceMetaData properties
            count++;
        }
    }

    protected void assertResourceRefs(String prefix, int size, ResourceReferencesMetaData resourceReferencesMetaData, boolean full, Mode mode) {
        assertNotNull("no resource refs are set", resourceReferencesMetaData);
        assertEquals(size, resourceReferencesMetaData.size());
        int count = 1;
        for (ResourceReferenceMetaData resourceReferenceMetaData : resourceReferencesMetaData) {
            if (mode != Mode.JBOSS_DTD) {
                assertId(prefix + "ResourceRef" + count, resourceReferenceMetaData);
                assertDescriptions(prefix + "ResourceRef" + count, resourceReferenceMetaData.getDescriptions());
            }
            assertEquals(prefix + "ResourceRef" + count + "Name", resourceReferenceMetaData.getResourceRefName());
            if (full) {
                assertEquals(prefix + "ResourceRef" + count + "Type", resourceReferenceMetaData.getType());
                if (count == 1) {
                    assertEquals(ResourceAuthorityType.Application, resourceReferenceMetaData.getResAuth());
                    assertEquals(ResourceSharingScopeType.Shareable, resourceReferenceMetaData.getResSharingScope());
                } else {
                    assertEquals(ResourceAuthorityType.Container, resourceReferenceMetaData.getResAuth());
                    assertEquals(ResourceSharingScopeType.Unshareable, resourceReferenceMetaData.getResSharingScope());
                }
            }
            assertResourceGroup(prefix + "ResourceRef" + count, resourceReferenceMetaData, full, count == 1, mode);
            ++count;
        }
    }

    protected void assertResourceEnvRefs(String prefix, int size, ResourceEnvironmentReferencesMetaData resourceEnvReferencesMetaData, boolean full, Mode mode) {
        assertNotNull("no resource env refs are set", resourceEnvReferencesMetaData);
        assertEquals(size, resourceEnvReferencesMetaData.size());
        int count = 1;
        for (ResourceEnvironmentReferenceMetaData resourceEnvReferenceMetaData : resourceEnvReferencesMetaData) {
            if (mode != Mode.JBOSS_DTD) {
                assertId(prefix + "ResourceEnvRef" + count, resourceEnvReferenceMetaData);
                assertDescriptions(prefix + "ResourceEnvRef" + count, resourceEnvReferenceMetaData.getDescriptions());
            }
            assertEquals(prefix + "ResourceEnvRef" + count + "Name", resourceEnvReferenceMetaData.getResourceEnvRefName());
            if (full) { assertEquals(prefix + "ResourceEnvRef" + count + "Type", resourceEnvReferenceMetaData.getType()); }
            assertResourceGroup(prefix + "ResourceEnvRef" + count, resourceEnvReferenceMetaData, full, count == 1, mode);
            ++count;
        }
    }

    protected void assertMessageDestinationRefs(String prefix, int size, MessageDestinationReferencesMetaData messageDestinationReferencesMetaData, boolean full, Mode mode) {
        assertNotNull("no message destination refs are set", messageDestinationReferencesMetaData);
        assertEquals(size, messageDestinationReferencesMetaData.size());
        int count = 1;
        for (MessageDestinationReferenceMetaData messageDestinationReferenceMetaData : messageDestinationReferencesMetaData) {
            if (mode != Mode.JBOSS_DTD) {
                assertId(prefix + "MessageDestinationRef" + count, messageDestinationReferenceMetaData);
                assertDescriptions(prefix + "MessageDestinationRef" + count, messageDestinationReferenceMetaData.getDescriptions());
            }
            assertEquals(prefix + "MessageDestinationRef" + count + "Name", messageDestinationReferenceMetaData.getMessageDestinationRefName());
            if (full) {
                assertEquals(prefix + "MessageDestinationRef" + count + "Type", messageDestinationReferenceMetaData.getType());
                if (count == 1) {
                    assertEquals(MessageDestinationUsageType.Consumes, messageDestinationReferenceMetaData.getMessageDestinationUsage());
                } else if (count == 2) { assertEquals(MessageDestinationUsageType.Produces, messageDestinationReferenceMetaData.getMessageDestinationUsage()); } else {
                    assertEquals(MessageDestinationUsageType.ConsumesProduces, messageDestinationReferenceMetaData.getMessageDestinationUsage());
                }
                assertEquals(prefix + "MessageDestinationRef" + count + "Link", messageDestinationReferenceMetaData.getLink());
            }
            assertResourceGroup(prefix + "MessageDestinationRef" + count, messageDestinationReferenceMetaData, full, count == 1, mode);
            ++count;
        }
    }

    @Deprecated
    protected void assertPersistenceContextRefs(String prefix, int size, PersistenceContextReferencesMetaData persistenceContextReferencesMetaData, Mode mode) {
        assertPersistenceContextRefs(prefix, size, persistenceContextReferencesMetaData, mode, JavaEEVersion.V6);
    }

    protected void assertPersistenceContextRefs(String prefix, int size, PersistenceContextReferencesMetaData persistenceContextReferencesMetaData, Mode mode, JavaEEVersion version) {
        assertNotNull(persistenceContextReferencesMetaData);
        assertEquals(size, persistenceContextReferencesMetaData.size());
        int count = 1;
        for (PersistenceContextReferenceMetaData persistenceContextReferenceMetaData : persistenceContextReferencesMetaData) {
            assertId(prefix + "PersistenceContextRef" + count, persistenceContextReferenceMetaData);
            assertDescriptions(prefix + "PersistenceContextRef" + count, persistenceContextReferenceMetaData.getDescriptions());
            assertEquals(prefix + "PersistenceContextRef" + count + "Name", persistenceContextReferenceMetaData.getPersistenceContextRefName());
            assertEquals(prefix + "PersistenceContextRef" + count + "Unit", persistenceContextReferenceMetaData.getPersistenceUnitName());
            if (version == JavaEEVersion.V7 || version == JavaEEVersion.V8 || version == JavaEEVersion.V9 || version == JavaEEVersion.V10) {
                PersistenceContextSynchronizationType type = count % 2 == 0 ? PersistenceContextSynchronizationType.Unsynchronized
                        : PersistenceContextSynchronizationType.Synchronized;
                assertEquals(type, persistenceContextReferenceMetaData.getPersistenceContextSynchronization());
            }
            if (count == 1) { assertEquals(PersistenceContextTypeDescription.TRANSACTION, persistenceContextReferenceMetaData.getPersistenceContextType()); } else {
                assertEquals(PersistenceContextTypeDescription.EXTENDED, persistenceContextReferenceMetaData.getPersistenceContextType());
            }
            assertProperties(prefix + "PersistenceContextRef" + count, 2, persistenceContextReferenceMetaData.getProperties());
            assertResourceGroup(prefix + "PersistenceContextRef" + count, persistenceContextReferenceMetaData, true, count == 1, mode);
            ++count;
        }
    }

    protected void assertDataSources(String prefix, DataSourcesMetaData metaDatas, Mode mode, boolean full, JavaEEVersion version) {
        if ((version != JavaEEVersion.V7 && version != JavaEEVersion.V8 && version != JavaEEVersion.V9 && version != JavaEEVersion.V10) || !full) {
            assertNull(metaDatas);
            return;
        }
        assertNotNull(metaDatas);
        assertEquals(2, metaDatas.size());
        int i = 1;
        for (DataSourceMetaData metaData : metaDatas) {
            assertNotNull(metaData);
            String dsPrefix = prefix+"DataSource" + i;
            //assertDescriptions(dsPrefix, ds.getDescriptions());
            Descriptions desc = metaData.getDescriptions();
            assertNotNull(desc);
            Description[] descArr = desc.value();
            assertNotNull(descArr);
            assertEquals(1, descArr.length);
            assertEquals("en-" + dsPrefix + "-desc", descArr[0].value());
            assertEquals(dsPrefix + "Name", metaData.getName());
            assertEquals(dsPrefix + "ClassName", metaData.getClassName());
            assertEquals(dsPrefix + "ServerName", metaData.getServerName());
            assertEquals(i, metaData.getPortNumber());
            assertEquals(dsPrefix + "DatabaseName", metaData.getDatabaseName());
            assertEquals("jdbc:" + dsPrefix + ":url", metaData.getUrl());
            assertEquals(dsPrefix + "User", metaData.getUser());
            assertEquals(dsPrefix + "Password", metaData.getPassword());
            assertProperties(dsPrefix, 2, metaData.getProperties());
            assertEquals(i, metaData.getLoginTimeout());
            assertEquals(i % 2 == 0, metaData.isTransactional());
            assertEquals(i % 2 == 0 ? IsolationLevelType.TRANSACTION_READ_COMMITTED : IsolationLevelType.TRANSACTION_READ_UNCOMMITTED, metaData.getIsolationLevel());
            assertEquals(i, metaData.getInitialPoolSize());
            assertEquals(i, metaData.getMaxPoolSize());
            assertEquals(i, metaData.getMinPoolSize());
            assertEquals(i, metaData.getMaxIdleTime());
            assertEquals(i, metaData.getMaxStatements());
            ++i;
        }
    }

    protected void assertAdministeredObjects(String prefix, AdministeredObjectsMetaData metaDatas, Mode mode, boolean full, JavaEEVersion version) {
        if ((version != JavaEEVersion.V7 && version != JavaEEVersion.V8 && version != JavaEEVersion.V9 && version != JavaEEVersion.V10) || !full) {
            assertNull(metaDatas);
            return;
        }
        prefix = prefix + "AdministeredObject";
        assertNotNull(metaDatas);
        assertTrue(metaDatas.size() < 4);
        int count = 1;
        for (AdministeredObjectMetaData metaData : metaDatas) {

            assertNotNull(metaData);
            final String metaDataPrefix = prefix + count;

            final Descriptions desc = metaData.getDescriptions();
            final String name = metaData.getName();
            final String interfaceName = metaData.getInterfaceName();
            final String className = metaData.getClassName();
            final String resourceAdapter = metaData.getResourceAdapter();
            final PropertiesMetaData properties = metaData.getProperties();

            // name
            assertEquals(metaDataPrefix + "Name", name);
            // class-name
            assertNotNull(className);
            assertEquals(metaDataPrefix + "ClassName", className);
            // resource-adapter
            assertNotNull(resourceAdapter);
            assertEquals(metaDataPrefix + "ResourceAdapter", resourceAdapter);
            if (count<3) {
                // interface-name
                assertNotNull(interfaceName);
                assertEquals(metaDataPrefix + "InterfaceName", interfaceName);
                // desc
                assertNotNull(desc);
                Description[] descArr = desc.value();
                assertNotNull(descArr);
                assertEquals(1, descArr.length);
                assertEquals(metaDataPrefix + "Desc", descArr[0].value());
                // properties
                assertProperties(metaDataPrefix,2,properties);
            } else {
                assertNull(interfaceName);
                assertNull(desc);
                assertNull(properties);
            }
            ++count;
        }
    }

    protected void assertConnectionFactories(String prefix, ConnectionFactoriesMetaData metaDatas, Mode mode, boolean full, JavaEEVersion version) {
        if (version == JavaEEVersion.V5 || version == JavaEEVersion.V6 || !full) {
            assertNull(metaDatas);
            return;
        }
        prefix = prefix + "ConnectionFactory";
        assertNotNull(metaDatas);
        assertTrue(metaDatas.size() < 5);
        int count = 1;
        for (ConnectionFactoryMetaData metaData : metaDatas) {

            assertNotNull(metaData);
            final String metaDataPrefix = prefix + count;

            final Descriptions desc = metaData.getDescriptions();
            final String name = metaData.getName();
            final String interfaceName = metaData.getInterfaceName();
            final String resourceAdapter = metaData.getResourceAdapter();
            final int maxPoolSize = metaData.getMaxPoolSize();
            final int minPoolSize = metaData.getMinPoolSize();
            final TransactionSupportType transactionSupport = metaData.getTransactionSupport();
            final PropertiesMetaData properties = metaData.getProperties();

            // name
            assertEquals(metaDataPrefix + "Name", name);
            // interface-name
            assertNotNull(interfaceName);
            assertEquals(metaDataPrefix + "InterfaceName", interfaceName);
            // resource-adapter
            assertNotNull(resourceAdapter);
            assertEquals(metaDataPrefix + "ResourceAdapter", resourceAdapter);
            if (count<4) {
                // desc
                assertNotNull(desc);
                Description[] descArr = desc.value();
                assertNotNull(descArr);
                assertEquals(1, descArr.length);
                assertEquals(metaDataPrefix + "Desc", descArr[0].value());
                // max-pool-size
                assertEquals(count, maxPoolSize);
                // min-pool-size
                assertEquals(count, minPoolSize);
                // transaction-support
                assertNotNull(transactionSupport);
                if (count==1) {
                    assertEquals(TransactionSupportType.NoTransaction, transactionSupport);
                } else if (count==2) {
                    assertEquals(TransactionSupportType.LocalTransaction, transactionSupport);
                } else {
                    assertEquals(TransactionSupportType.XATransaction, transactionSupport);
                }
                // properties
                assertProperties(metaDataPrefix,2,properties);
            } else {
                assertNull(desc);
                assertEquals(JMSConnectionFactoryMetaData.DEFAULT_MAX_POOL_SIZE, maxPoolSize);
                assertEquals(JMSConnectionFactoryMetaData.DEFAULT_MIN_POOL_SIZE, minPoolSize);
                assertNotNull(transactionSupport);
                assertEquals(TransactionSupportType.NoTransaction, transactionSupport);
                assertNull(properties);
            }
            ++count;
        }
    }

    protected void assertJMSConnectionFactories(String prefix, JMSConnectionFactoriesMetaData metaDatas, Mode mode, boolean full, JavaEEVersion version) {
        if (version == JavaEEVersion.V5 || version == JavaEEVersion.V6 || !full) {
            assertNull(metaDatas);
            return;
        }
        assertNotNull(metaDatas);
        assertTrue(metaDatas.size() < 4);
        prefix = prefix + "JmsConnectionFactory";
        int count = 1;
        for (JMSConnectionFactoryMetaData metaData : metaDatas) {

            assertNotNull(metaData);
            final String metaDataPrefix = prefix + count;

            final Descriptions desc = metaData.getDescriptions();
            final String name = metaData.getName();
            final String className = metaData.getClassName();
            final String interfaceName = metaData.getInterfaceName();
            final String resourceAdapter = metaData.getResourceAdapter();
            final String user = metaData.getUser();
            final String password = metaData.getPassword();
            final String clientId = metaData.getClientId();
            final PropertiesMetaData properties = metaData.getProperties();
            final boolean transactional = metaData.isTransactional();
            final int maxPoolSize = metaData.getMaxPoolSize();
            final int minPoolSize = metaData.getMinPoolSize();

            assertEquals(metaDataPrefix + "Name", name);
            if (count<3) {
                // desc
                assertNotNull(desc);
                Description[] descArr = desc.value();
                assertNotNull(descArr);
                assertEquals(1, descArr.length);
                assertEquals(metaDataPrefix + "Desc", descArr[0].value());
                // interface-name
                assertNotNull(interfaceName);
                if (count == 1) {
                    assertEquals(getQueueConnectionFactoryInterfaceName(), interfaceName);
                } else {
                    assertEquals(getTopicConnectionFactoryInterfaceName(), interfaceName);
                }
                // class-name
                assertNotNull(className);
                assertEquals(metaDataPrefix + "ClassName", className);
                // resource-adapter
                assertNotNull(resourceAdapter);
                assertEquals(metaDataPrefix + "ResourceAdapter", resourceAdapter);
                // user
                assertNotNull(user);
                assertEquals(metaDataPrefix + "User", user);
                // password
                assertNotNull(password);
                assertEquals(metaDataPrefix + "Password", password);
                // client-id
                assertNotNull(clientId);
                assertEquals(metaDataPrefix + "ClientId", clientId);
                // properties
                assertProperties(metaDataPrefix,2,properties);
                // transactional
                assertEquals(count % 2 != 0, transactional);
                // max-pool-size
                assertEquals(count, maxPoolSize);
                // min-pool-size
                assertEquals(count, minPoolSize);
            } else {
                assertNull(desc);
                assertNotNull(interfaceName);
                assertEquals(getDefaultInterfaceName(), interfaceName);
                assertNull(className);
                assertNull(resourceAdapter);
                assertNull(user);
                assertNull(password);
                assertNull(clientId);
                assertNull(properties);
                assertEquals(JMSConnectionFactoryMetaData.DEFAULT_TRANSACTIONAL, transactional);
                assertEquals(JMSConnectionFactoryMetaData.DEFAULT_MAX_POOL_SIZE, maxPoolSize);
                assertEquals(JMSConnectionFactoryMetaData.DEFAULT_MIN_POOL_SIZE, minPoolSize);
            }
            ++count;
        }
    }

    protected String getQueueConnectionFactoryInterfaceName() {
        return JMSConnectionFactoryMetaData.QUEUE_CONNECTION_FACTORY_INTERFACE_NAME;
    }

    protected String getTopicConnectionFactoryInterfaceName() {
        return JMSConnectionFactoryMetaData.TOPIC_CONNECTION_FACTORY_INTERFACE_NAME;
    }

    protected String getDefaultInterfaceName() {
        return JMSConnectionFactoryMetaData.DEFAULT_INTERFACE_NAME;
    }

    protected void assertJMSDestinations(String prefix, JMSDestinationsMetaData metaDatas, Mode mode, boolean full, JavaEEVersion version) {
        if (version == JavaEEVersion.V5 || version == JavaEEVersion.V6 || !full) {
            assertNull(metaDatas);
            return;
        }
        assertNotNull(metaDatas);
        assertTrue(metaDatas.size() < 4);
        prefix = prefix + "JmsDestination";
        int count = 1;
        for (JMSDestinationMetaData metaData : metaDatas) {

            assertNotNull(metaData);
            final String metaDataPrefix = prefix + count;

            final Descriptions desc = metaData.getDescriptions();
            final String name = metaData.getName();
            final String interfaceName = metaData.getInterfaceName();
            final String className = metaData.getClassName();
            final String resourceAdapter = metaData.getResourceAdapter();
            final String destinationName = metaData.getDestinationName();
            final PropertiesMetaData properties = metaData.getProperties();

            assertEquals(metaDataPrefix + "Name", name);
            assertNotNull(interfaceName);
            if (count<3) {
                // desc
                assertNotNull(desc);
                Description[] descArr = desc.value();
                assertNotNull(descArr);
                assertEquals(1, descArr.length);
                assertEquals(metaDataPrefix + "Desc", descArr[0].value());
                // interface-name
                assertEquals(getQueueInterfaceName(), interfaceName);
                // class-name
                assertNotNull(className);
                assertEquals(metaDataPrefix + "ClassName", className);
                // resource-adapter
                assertNotNull(resourceAdapter);
                assertEquals(metaDataPrefix + "ResourceAdapter", resourceAdapter);
                // destination-name
                assertNotNull(destinationName);
                assertEquals(metaDataPrefix + "DestinationName", destinationName);
                // properties
                assertProperties(metaDataPrefix,2,properties);
            } else {
                assertNull(desc);
                assertEquals(getTopicInterfaceName(), interfaceName);
                assertNull(className);
                assertNull(resourceAdapter);
                assertNull(destinationName);
                assertNull(properties);
            }
            ++count;
        }
    }

    protected String getQueueInterfaceName() {
        return JMSDestinationMetaData.QUEUE_INTERFACE_NAME;
    }

    protected String getTopicInterfaceName() {
        return JMSDestinationMetaData.TOPIC_INTERFACE_NAME;
    }

    protected void assertMailSessions(String prefix, MailSessionsMetaData metaDatas, Mode mode, boolean full, JavaEEVersion version) {
        if (version == JavaEEVersion.V5 || version == JavaEEVersion.V6 || !full) {
            assertNull(metaDatas);
            return;
        }
        assertNotNull(metaDatas);
        assertTrue(metaDatas.size() < 4);
        prefix = prefix + "MailSession";
        int count = 1;
        for (MailSessionMetaData metaData : metaDatas) {

            assertNotNull(metaData);
            final String metaDataPrefix = prefix + count;

            final Descriptions desc = metaData.getDescriptions();
            final String name = metaData.getName();
            final String storeProtocol = metaData.getStoreProtocol();
            final String storeProtocolClass = metaData.getStoreProtocolClass();
            final String transportProtocol = metaData.getTransportProtocol();
            final String transportProtocolClass = metaData.getTransportProtocolClass();
            final String host = metaData.getHost();
            final String user = metaData.getUser();
            final String password = metaData.getPassword();
            final String from = metaData.getFrom();
            final PropertiesMetaData properties = metaData.getProperties();

            assertEquals(metaDataPrefix + "Name", name);
            if (count<3) {
                // desc
                assertNotNull(desc);
                Description[] descArr = desc.value();
                assertNotNull(descArr);
                assertEquals(1, descArr.length);
                assertEquals(metaDataPrefix + "Desc", descArr[0].value());
                // store-protocol
                assertNotNull(storeProtocol);
                assertEquals(metaDataPrefix + "StoreProtocol", storeProtocol);
                // store-protocol-class
                assertNotNull(storeProtocolClass);
                assertEquals(metaDataPrefix + "StoreProtocolClass", storeProtocolClass);
                // transport-protocol
                assertNotNull(transportProtocol);
                assertEquals(metaDataPrefix + "TransportProtocol", transportProtocol);
                // transport-protocol-class
                assertNotNull(transportProtocolClass);
                assertEquals(metaDataPrefix + "TransportProtocolClass", transportProtocolClass);
                // host
                assertNotNull(host);
                assertEquals(metaDataPrefix + "Host", host);
                // user
                assertNotNull(user);
                assertEquals(metaDataPrefix + "User", user);
                // password
                assertNotNull(password);
                assertEquals(metaDataPrefix + "Password", password);
                // from
                assertNotNull(from);
                assertEquals(metaDataPrefix + "From", from);
                // properties
                assertProperties(metaDataPrefix,2,properties);
            } else {
                assertNull(desc);
                assertNull(storeProtocol);
                assertNull(storeProtocolClass);
                assertNull(transportProtocol);
                assertNull(transportProtocolClass);
                assertNull(host);
                assertNull(user);
                assertNull(password);
                assertNull(from);
                assertNull(properties);
            }
            ++count;
        }
    }

    protected void assertContextServices(String prefix, ContextServicesMetaData metaDatas, Mode mode, boolean full, JavaEEVersion version) {
        if (version != JavaEEVersion.V10 || !full) {
            assertNull(metaDatas);
            return;
        }
        assertNotNull(metaDatas);
        assertTrue(metaDatas.size() < 4);
        prefix = prefix + "ContextService";
        int count = 1;
        for (ContextServiceMetaData metaData : metaDatas) {

            assertNotNull(metaData);
            final String metaDataPrefix = prefix + count;

            final Descriptions desc = metaData.getDescriptions();
            final String name = metaData.getName();
            final Set<String> cleared = metaData.getCleared();
            final Set<String> propagated = metaData.getPropagated();
            final Set<String> unchanged = metaData.getUnchanged();
            final PropertiesMetaData properties = metaData.getProperties();
            final List<String> qualifier = metaData.getQualifier();

            assertEquals(metaDataPrefix + "-name", name);
            if (count<3) {
                // desc
                assertNotNull(desc);
                Description[] descArr = desc.value();
                assertNotNull(descArr);
                assertEquals(1, descArr.length);
                assertEquals(metaDataPrefix + "-desc", descArr[0].value());
                // cleared
                assertNotNull(cleared);
                assertEquals(cleared.size(), 1);
                assertEquals(metaDataPrefix + "-cleared", cleared.iterator().next());
                // propagated
                assertNotNull(propagated);
                assertEquals(propagated.size(), 1);
                assertEquals(metaDataPrefix + "-propagated", propagated.iterator().next());
                // unchanged
                assertNotNull(unchanged);
                assertEquals(unchanged.size(), 1);
                assertEquals(metaDataPrefix + "-unchanged", unchanged.iterator().next());
                // properties
                assertProperties(metaDataPrefix,2,properties);
                if (version.ordinal() > JavaEEVersion.V10.ordinal()) {
                    // qualifier
                    assertQualifier(metaDataPrefix, 2, qualifier);
                }
            } else {
                assertNull(desc);
                assertNull(cleared);
                assertNull(propagated);
                assertNull(unchanged);
                assertNull(properties);
                assertNull(qualifier);
            }
            ++count;
        }
    }

    protected void assertManagedExecutors(String prefix, ManagedExecutorsMetaData metaDatas, Mode mode, boolean full, JavaEEVersion version) {
        if (version != JavaEEVersion.V10 || !full) {
            assertNull(metaDatas);
            return;
        }
        assertNotNull(metaDatas);
        assertTrue(metaDatas.size() < 4);
        prefix = prefix + "ManagedExecutor";
        int count = 1;
        for (ManagedExecutorMetaData metaData : metaDatas) {

            assertNotNull(metaData);
            final String metaDataPrefix = prefix + count;

            final Descriptions desc = metaData.getDescriptions();
            final String name = metaData.getName();
            final String contextServiceRef = metaData.getContextServiceRef();
            final Integer hungTaskThreshold = metaData.getHungTaskThreshold();
            final Integer maxAsync = metaData.getMaxAsync();
            final PropertiesMetaData properties = metaData.getProperties();
            final List<String> qualifier = metaData.getQualifier();
            final Boolean virtual = metaData.getVirtual();

            assertEquals(metaDataPrefix + "-name", name);
            if (count<3) {
                // desc
                assertNotNull(desc);
                Description[] descArr = desc.value();
                assertNotNull(descArr);
                assertEquals(1, descArr.length);
                assertEquals(metaDataPrefix + "-desc", descArr[0].value());
                // contextServiceRef
                assertNotNull(contextServiceRef);
                assertEquals(metaDataPrefix + "-contextServiceRef", contextServiceRef);
                // hungTaskThreshold
                assertNotNull(hungTaskThreshold);
                assertEquals(count, hungTaskThreshold.intValue());
                // maxAsync
                assertNotNull(maxAsync);
                assertEquals(count, maxAsync.intValue());
                // properties
                assertProperties(metaDataPrefix,2,properties);
                if (version.ordinal() > JavaEEVersion.V10.ordinal()) {
                    // qualifier
                    assertQualifier(metaDataPrefix, 2, qualifier);
                    // virtual
                    assertVirtual(count, virtual);
                }
            } else {
                assertNull(desc);
                assertNull(contextServiceRef);
                assertNull(hungTaskThreshold);
                assertNull(maxAsync);
                assertNull(properties);
                assertNull(qualifier);
                assertNull(virtual);
            }
            ++count;
        }
    }

    protected void assertManagedScheduledExecutors(String prefix, ManagedScheduledExecutorsMetaData metaDatas, Mode mode, boolean full, JavaEEVersion version) {
        if (version != JavaEEVersion.V10 || !full) {
            assertNull(metaDatas);
            return;
        }
        assertNotNull(metaDatas);
        assertTrue(metaDatas.size() < 4);
        prefix = prefix + "ManagedScheduledExecutor";
        int count = 1;
        for (ManagedExecutorMetaData metaData : metaDatas) {

            assertNotNull(metaData);
            final String metaDataPrefix = prefix + count;

            final Descriptions desc = metaData.getDescriptions();
            final String name = metaData.getName();
            final String contextServiceRef = metaData.getContextServiceRef();
            final Integer hungTaskThreshold = metaData.getHungTaskThreshold();
            final Integer maxAsync = metaData.getMaxAsync();
            final PropertiesMetaData properties = metaData.getProperties();
            final List<String> qualifier = metaData.getQualifier();
            final Boolean virtual = metaData.getVirtual();

            assertEquals(metaDataPrefix + "-name", name);
            if (count<3) {
                // desc
                assertNotNull(desc);
                Description[] descArr = desc.value();
                assertNotNull(descArr);
                assertEquals(1, descArr.length);
                assertEquals(metaDataPrefix + "-desc", descArr[0].value());
                // contextServiceRef
                assertNotNull(contextServiceRef);
                assertEquals(metaDataPrefix + "-contextServiceRef", contextServiceRef);
                // hungTaskThreshold
                assertNotNull(hungTaskThreshold);
                assertEquals(count, hungTaskThreshold.intValue());
                // maxAsync
                assertNotNull(maxAsync);
                assertEquals(count, maxAsync.intValue());
                // properties
                assertProperties(metaDataPrefix,2,properties);
                if (version.ordinal() > JavaEEVersion.V10.ordinal()) {
                    // qualifier
                    assertQualifier(metaDataPrefix, 2, qualifier);
                    // virtual
                    assertVirtual(count, virtual);
                }
            } else {
                assertNull(desc);
                assertNull(contextServiceRef);
                assertNull(hungTaskThreshold);
                assertNull(maxAsync);
                assertNull(properties);
                assertNull(qualifier);
                assertNull(virtual);
            }
            ++count;
        }
    }

    protected void assertManagedThreadFactories(String prefix, ManagedThreadFactoriesMetaData metaDatas, Mode mode, boolean full, JavaEEVersion version) {
        if (version != JavaEEVersion.V10 || !full) {
            assertNull(metaDatas);
            return;
        }
        assertNotNull(metaDatas);
        assertTrue(metaDatas.size() < 4);
        prefix = prefix + "ManagedThreadFactory";
        int count = 1;
        for (ManagedThreadFactoryMetaData metaData : metaDatas) {

            assertNotNull(metaData);
            final String metaDataPrefix = prefix + count;

            final Descriptions desc = metaData.getDescriptions();
            final String name = metaData.getName();
            final String contextServiceRef = metaData.getContextServiceRef();
            final int priority = metaData.getPriority();
            final PropertiesMetaData properties = metaData.getProperties();
            final List<String> qualifier = metaData.getQualifier();
            final Boolean virtual = metaData.getVirtual();

            assertEquals(metaDataPrefix + "-name", name);
            if (count<3) {
                // desc
                assertNotNull(desc);
                Description[] descArr = desc.value();
                assertNotNull(descArr);
                assertEquals(1, descArr.length);
                assertEquals(metaDataPrefix + "-desc", descArr[0].value());
                // contextServiceRef
                assertNotNull(contextServiceRef);
                assertEquals(metaDataPrefix + "-contextServiceRef", contextServiceRef);
                // priority
                assertEquals(count, priority);
                // properties
                assertProperties(metaDataPrefix,2,properties);
                if (version.ordinal() > JavaEEVersion.V10.ordinal()) {
                    // qualifier
                    assertQualifier(metaDataPrefix, 2, qualifier);
                    // virtual
                    assertVirtual(count, virtual);
                }
            } else {
                assertNull(desc);
                assertNull(contextServiceRef);
                assertEquals(priority, Thread.NORM_PRIORITY);
                assertNull(properties);
                assertNull(qualifier);
                assertNull(virtual);
            }
            ++count;
        }
    }

    protected void assertPersistenceUnitRefs(String prefix, int size, PersistenceUnitReferencesMetaData persistenceUnitReferencesMetaData, Mode mode) {
        assertNotNull("no persistence unit refs are set", persistenceUnitReferencesMetaData);
        assertEquals(size, persistenceUnitReferencesMetaData.size());
        int count = 1;
        for (PersistenceUnitReferenceMetaData persistenceUnitReferenceMetaData : persistenceUnitReferencesMetaData) {
            assertId(prefix + "PersistenceUnitRef" + count, persistenceUnitReferenceMetaData);
            assertDescriptions(prefix + "PersistenceUnitRef" + count, persistenceUnitReferenceMetaData.getDescriptions());
            assertEquals(prefix + "PersistenceUnitRef" + count + "Name", persistenceUnitReferenceMetaData.getPersistenceUnitRefName());
            assertEquals(prefix + "PersistenceUnitRef" + count + "Unit", persistenceUnitReferenceMetaData.getPersistenceUnitName());
            assertResourceGroup(prefix + "PersistenceUnitRef" + count, persistenceUnitReferenceMetaData, true, count == 1, mode);
            ++count;
        }
    }

    protected void assertProperties(String prefix, int size, PropertiesMetaData propertiesMetaData) {
        assertNotNull(propertiesMetaData);
        assertEquals(size, propertiesMetaData.size());
        int count = 1;
        for (PropertyMetaData property : propertiesMetaData) {
            assertId(prefix + "Property" + count, property);
            assertEquals(prefix + "Property" + count + "Name", property.getName());
            assertEquals(prefix + "Property" + count + "Value", property.getValue());
            ++count;
        }
    }

    protected void assertQualifier(String prefix, int size, List<String> qualifierMetaData) {
        assertNotNull(qualifierMetaData);
        assertEquals(size, qualifierMetaData.size());
        int count = 1;
        for (String qualifier : qualifierMetaData) {
            assertEquals(prefix + "Qualifier" + count, qualifier);
            ++count;
        }
    }

    protected void assertVirtual(int count, Boolean virtual) {
        assertNotNull(virtual);
        if (count == 1) {
            assertTrue(virtual);
        } else {
            assertFalse(virtual);
        }
    }

    protected void assertResourceGroupNoJndiName(String prefix, ResourceInjectionMetaData resourceInjectionMetaData, boolean full, boolean first) {
        assertNotNull(resourceInjectionMetaData);
        assertResourceInjectionTargets(prefix, 2, resourceInjectionMetaData.getInjectionTargets());
    }

    protected void assertResourceGroup(String prefix, ResourceInjectionMetaData resourceInjectionMetaData, boolean full, boolean first, Mode mode) {
        assertResourceGroupJndiName(prefix, resourceInjectionMetaData, full, first, mode);
    }

    private void assertResourceGroupJndiName(String prefix, ResourceInjectionMetaData resourceInjectionMetaData, boolean full, boolean first, Mode mode) {
        assertResourceGroupNoJndiName(prefix, resourceInjectionMetaData, full, first);
        assertJndiName(prefix, full, resourceInjectionMetaData.getMappedName(), mode);
    }

    protected void assertResourceInjectionTargets(String prefix, int size, Set<ResourceInjectionTargetMetaData> targets) {
        assertNotNull(targets);
        assertEquals(targets.toString(), size, targets.size());
        int count = 0;
        while (++count <= targets.size()) {
            ResourceInjectionTargetMetaData expected = new ResourceInjectionTargetMetaData();
            expected.setInjectionTargetClass(prefix + "Injection" + count + "Class");
            expected.setInjectionTargetName(prefix + "Injection" + count + "Name");
            assertTrue(targets.contains(expected));
        }
    }

    protected void assertResourceInjectionTarget(String prefix, ResourceInjectionTargetMetaData target) {
        assertNotNull(target);
        assertEquals(prefix + "Class", target.getInjectionTargetClass());
        assertEquals(prefix + "Name", target.getInjectionTargetName());
    }

    protected void assertJndiName(String prefix, boolean full, String jndiName, Mode mode) {
        assertNotNull(jndiName);
//      switch(mode)
//      {
//         case JBOSS:
//            assertEquals(prefix, prefix + "JBoss", jndiName);
//            break;
//         case SPEC:
//            assertEquals(prefix, prefix + "MappedName", jndiName);
//            break;
//         default:
//            throw new IllegalArgumentException("unknown mode " + mode);
//      }
        // Getting too convoluted when there is a jboss override...
        String expectedMappedName = prefix + "MappedName";
        String expectedJndiName = prefix + "JndiName";
        assertTrue(prefix + " has jndi/mapped name of "
                + expectedMappedName + " or " + expectedJndiName,
                jndiName.equals(expectedJndiName) || jndiName.equals(expectedMappedName));
    }

    protected void assertAnnotations(String prefix, int size, AnnotationsMetaData annotationsMetaData) {
        assertNotNull(annotationsMetaData);
        assertEquals(size, annotationsMetaData.size());
        int count = 1;
        for (AnnotationMetaData annotation : annotationsMetaData) {
            assertId(prefix + "Annotation" + count, annotation);
            assertDescriptions(prefix + "Annotation" + count, annotation.getDescriptions());
            assertEquals(prefix + "Annotation" + count + "Class", annotation.getAnnotationClass());
            assertEquals(prefix + "Annotation" + count + "Impl", annotation.getAnnotationImplementationClass());
            assertResourceInjectionTarget(prefix + "Annotation" + count + "InjectionTarget", annotation.getInjectionTarget());
            assertAnnotationProperties(prefix + "Annotation" + count, 2, annotation.getProperties());
            ++count;
        }
    }

    private void assertAnnotationProperties(String prefix, int size, AnnotationPropertiesMetaData annotationPropertiesMetaData) {
        assertNotNull(annotationPropertiesMetaData);
        assertEquals(size, annotationPropertiesMetaData.size());
        int count = 1;
        for (AnnotationPropertyMetaData annotationProperty : annotationPropertiesMetaData) {
            assertId(prefix + "Property" + count, annotationProperty);
            assertDescriptions(prefix + "Property" + count, annotationProperty.getDescriptions());
            assertEquals(prefix + "Property" + count + "Name", annotationProperty.getPropertyName());
            assertEquals(prefix + "Property" + count + "Value", annotationProperty.getPropertyValue());
            ++count;
        }
    }

    protected void assertJndiRefs(String prefix, int size, JndiRefsMetaData jndiRefsMetaData, Mode mode) {
        assertNotNull(jndiRefsMetaData);
        assertEquals(size, jndiRefsMetaData.size());
        int count = 1;
        for (JndiRefMetaData jndiRef : jndiRefsMetaData) {
            assertId(prefix + "JndiRef" + count, jndiRef);
            assertDescriptions(prefix + "JndiRef" + count, jndiRef.getDescriptions());
            assertEquals(prefix + "JndiRef" + count + "Name", jndiRef.getJndiRefName());
            assertResourceGroupJndiName(prefix + "JndiRef" + count, jndiRef, true, count == 1, mode);
            ++count;
        }
    }
}
