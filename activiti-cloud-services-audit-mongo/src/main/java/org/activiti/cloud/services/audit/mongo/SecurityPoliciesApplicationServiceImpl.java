package org.activiti.cloud.services.audit.mongo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.api.runtime.shared.identity.UserGroupManager;
import org.activiti.api.runtime.shared.security.SecurityManager;
import org.activiti.cloud.services.audit.mongo.events.AuditEventDocument;
import org.activiti.cloud.services.audit.mongo.repository.EventQueryDslBuilder;
import org.activiti.cloud.services.audit.mongo.repository.SearchOperation;
import org.activiti.cloud.services.audit.mongo.repository.SpecSearchCriteria;
import org.activiti.spring.security.policies.BaseSecurityPoliciesManagerImpl;
import org.activiti.spring.security.policies.SecurityPoliciesManager;
import org.activiti.spring.security.policies.SecurityPolicyAccess;
import org.activiti.spring.security.policies.conf.SecurityPoliciesProperties;
import org.springframework.stereotype.Component;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;

@Component
public class SecurityPoliciesApplicationServiceImpl extends BaseSecurityPoliciesManagerImpl implements SecurityPoliciesManager {

    public SecurityPoliciesApplicationServiceImpl(UserGroupManager userGroupManager,
                                                  SecurityManager securityManager,
                                                  SecurityPoliciesProperties securityPoliciesProperties) {
        super(userGroupManager, securityManager, securityPoliciesProperties);
    }

    /*
     * Apply Filters for Security Policies (configured in application.properties
     * Steps
     *  - If no Security Policies or no User, return unmodified
     *  - For Each Security Policy
     *    - Get Process Definitions associated with the policy
     *    - If it was not a wildcard
     *      - Add Policy for Service Name, ProcessDefinition pair
     *    - If it was a wildcard
     *      - Add Policy for Service Name only
     *  - If no other policies applied
     *    - Add Impossible filter so the user doesn't get any data
     */
    public Predicate createSearchSpecWithSecurity(Predicate expression,
                                                  SecurityPolicyAccess securityPolicy) {
        if (!arePoliciesDefined()) {
            return expression;
        }
        Map<String, Set<String>> restrictions = getAllowedKeys(securityPolicy);

        for (String serviceName : restrictions.keySet()) {

            Set<String> defKeys = restrictions.get(serviceName);
            //will filter by app name and will also filter by definition keys if no wildcard,
            if (defKeys != null && defKeys.size() > 0 && !defKeys.contains(securityPoliciesProperties.getWildcard())) {
                return createApplicationProcessDefSecuritySearchSpec(expression, serviceName, defKeys);
            } else if (defKeys != null && defKeys.contains(securityPoliciesProperties.getWildcard())) { //will filter by app name if wildcard is set
                return createApplicationSecuritySearchSpec(expression, serviceName);
            }
        }
        //policies are defined but none are applicable
        //user should not see anything so give unsatisfiable condition
        return createImpossibleSearchSpec(expression);
    }

    private Predicate createApplicationSecuritySearchSpec(Predicate expression,
                                                          String serviceName) {
        PathBuilder<AuditEventDocument> builder = new PathBuilder<AuditEventDocument>(AuditEventDocument.class,
                "document");
        if (expression == null) {
            return builder.getString("serviceName").eq(serviceName);
        } else {
            return new BooleanBuilder().orAllOf(expression, builder.getString("serviceName").eq(serviceName));
        }
    }

    private Predicate createApplicationProcessDefSecuritySearchSpec(Predicate expression,
                                                                    String serviceName,
                                                                    Set<String> processDefinitions) {
        List<BooleanExpression> expressions = new ArrayList<>();
        for (String processDef : processDefinitions) {
            //don't actually have definitionKey in the event but do have definitionId which should contain it
            // format is e.g. SimpleProcess:version:id
            // and fact we're here means can't be wildcard
            BooleanExpression processDefinitionMatchPredicate = new PathBuilder<AuditEventDocument>(AuditEventDocument.class,
                    "document").getString("processDefinitionId").like(
                                                                      processDef + "%");

            BooleanExpression appNameMatchPredicate = new PathBuilder<AuditEventDocument>(AuditEventDocument.class,
                    "document").getString("serviceName")
                    .eq(serviceName);

            BooleanExpression appRestrictionPredicate = processDefinitionMatchPredicate.and(appNameMatchPredicate);
            expressions.add(appRestrictionPredicate);
        }

        if (expression == null) {
            return Expressions.anyOf(expressions.toArray(new BooleanExpression[expressions.size()]));
        } else {
            return new BooleanBuilder().or(expression).orAllOf(expressions.toArray(new BooleanExpression[expressions
                                                                                                         .size()]));
        }
    }

    private Predicate createImpossibleSearchSpec(Predicate expression) {
        EventQueryDslBuilder builder = new EventQueryDslBuilder();
        builder.with(new SpecSearchCriteria(SearchOperation.AND_OPERATOR,
                                            "processInstanceId",
                                            SearchOperation.EQUALITY,
                "0"));
        builder.with(new SpecSearchCriteria(SearchOperation.AND_OPERATOR,
                                            "processInstanceId",
                                            SearchOperation.EQUALITY,
                "0"));
        if (expression == null) {
            return builder.build();
        } else {
            return new BooleanBuilder().andAnyOf(expression, builder.build());
        }
    }

    @Override
    public boolean canRead(String arg0) {
        //should always use canWrite(processDefinitionKey, appName)
        return false;
    }

    @Override
    public boolean canWrite(String arg0) {
        //should always use canRead(processDefinitionKey, appName)
        return false;
    }

}