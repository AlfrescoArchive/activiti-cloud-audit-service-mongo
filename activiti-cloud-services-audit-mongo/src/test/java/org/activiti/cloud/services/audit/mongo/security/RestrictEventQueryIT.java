package org.activiti.cloud.services.audit.mongo.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Iterator;

import org.activiti.api.runtime.shared.identity.UserGroupManager;
import org.activiti.api.runtime.shared.security.SecurityManager;
import org.activiti.cloud.services.audit.mongo.SecurityPoliciesApplicationServiceImpl;
import org.activiti.cloud.services.audit.mongo.events.AuditEventDocument;
import org.activiti.cloud.services.audit.mongo.events.ProcessStartedEventDocument;
import org.activiti.cloud.services.audit.mongo.repository.EventsRepository;
import org.activiti.spring.security.policies.SecurityPolicyAccess;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.querydsl.core.types.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("classpath:test-application.properties")
@SpringBootTest
@EnableConfigurationProperties
@EnableMongoRepositories(basePackageClasses = EventsRepository.class)
@EntityScan("org.activiti")
@EnableAutoConfiguration
public class RestrictEventQueryIT {

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private SecurityPoliciesApplicationServiceImpl securityPoliciesApplicationService;

    @MockBean
    private SecurityManager securityManager;

    @MockBean
    private UserGroupManager userGroupManager;

    @Test
    public void shouldGetProcessInstancesWhenPermitted() throws Exception {

        ProcessStartedEventDocument eventEntity = new ProcessStartedEventDocument();
        eventEntity.setId(String.valueOf(15L));
        eventEntity.setProcessDefinitionId("defKey1");
        eventEntity.setServiceName("audit");

        eventsRepository.save(eventEntity);

        when(securityManager.getAuthenticatedUserId()).thenReturn("testuser");

        Predicate spec = securityPoliciesApplicationService.createSearchSpecWithSecurity(null,
                                                                                         SecurityPolicyAccess.READ);

        Iterable<AuditEventDocument> iterable = eventsRepository.findAll(spec);
        assertThat(iterable.iterator().hasNext()).isTrue();
    }

    @Test
    public void shouldGetProcessInstancesWhenUserPermittedByWildcard() throws Exception {

        ProcessStartedEventDocument eventEntity = new ProcessStartedEventDocument();
        eventEntity.setId(String.valueOf(16L));
        eventEntity.setProcessDefinitionId("defKeyWild");
        eventEntity.setServiceName("audit-wild");

        eventsRepository.save(eventEntity);

        when(securityManager.getAuthenticatedUserId()).thenReturn("hruser");

        Predicate spec = securityPoliciesApplicationService.createSearchSpecWithSecurity(null,
                                                                                         SecurityPolicyAccess.READ);

        Iterable<AuditEventDocument> iterable = eventsRepository.findAll(spec);

        assertThat(iterable.iterator().hasNext()).isTrue();
    }

    @Test
    public void shouldGetProcessInstancesWhenGroupPermittedByWildcard() throws Exception {

        ProcessStartedEventDocument eventEntity = new ProcessStartedEventDocument();
        eventEntity.setId(String.valueOf(17L));
        eventEntity.setProcessDefinitionId("defKeyWild");
        eventEntity.setServiceName("audit-wild");

        eventsRepository.save(eventEntity);

        when(securityManager.getAuthenticatedUserId()).thenReturn("bobinhr");
        when(userGroupManager.getUserGroups("bobinhr")).thenReturn(Collections.singletonList("hrgroup"));

        Predicate spec = securityPoliciesApplicationService.createSearchSpecWithSecurity(null,
                                                                                         SecurityPolicyAccess.READ);

        Iterable<AuditEventDocument> iterable = eventsRepository.findAll(spec);
        assertThat(iterable.iterator().hasNext()).isTrue();
    }

    @Test
    public void shouldNotGetProcessInstancesWhenPolicyNotForUser() throws Exception {

        ProcessStartedEventDocument eventEntity = new ProcessStartedEventDocument();
        eventEntity.setId(String.valueOf(18L));
        eventEntity.setProcessDefinitionId("defKeyWild");
        eventEntity.setServiceName("audit-wild");

        eventsRepository.save(eventEntity);

        when(securityManager.getAuthenticatedUserId()).thenReturn("testuser");

        Predicate spec = securityPoliciesApplicationService.createSearchSpecWithSecurity(null,
                                                                                         SecurityPolicyAccess.READ);

        Iterable<AuditEventDocument> iterable = eventsRepository.findAll(spec);

        //this user should see proc instances - but not for test-cmd-endpoint-wild

        Iterator<AuditEventDocument> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            AuditEventDocument auditEvent = iterator.next();
            assertThat(auditEvent.getServiceName()).isNotEqualToIgnoringCase("audit-wild");
            assertThat(auditEvent.getServiceName()).isEqualToIgnoringCase("audit");
        }
    }

    @Test
    public void shouldNotGetProcessInstancesWhenNotPermitted() throws Exception {

        when(securityManager.getAuthenticatedUserId()).thenReturn("intruder");

        Predicate spec = securityPoliciesApplicationService.createSearchSpecWithSecurity(null,
                                                                                         SecurityPolicyAccess.READ);

        Iterable<AuditEventDocument> iterable = eventsRepository.findAll(spec);
        assertThat(iterable.iterator().hasNext()).isFalse();
    }

    @Test
    public void shouldGetProcessInstancesWhenMatchesFullServiceName() throws Exception {

        ProcessStartedEventDocument eventEntity = new ProcessStartedEventDocument();
        eventEntity.setId(String.valueOf(21L));
        eventEntity.setProcessDefinitionId("defKey2");
        eventEntity.setServiceName("audit");

        eventsRepository.save(eventEntity);

        when(securityManager.getAuthenticatedUserId()).thenReturn("hruser");
        Predicate spec = securityPoliciesApplicationService.createSearchSpecWithSecurity(null,
                                                                                         SecurityPolicyAccess.READ);

        Iterable<AuditEventDocument> iterable = eventsRepository.findAll(spec);

        assertThat(iterable.iterator().hasNext()).isTrue();
    }
}
