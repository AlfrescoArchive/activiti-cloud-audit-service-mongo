package org.activiti.cloud.services.audit.mongo.repository;

import java.util.ArrayList;
import java.util.List;

import org.activiti.cloud.services.audit.mongo.events.AuditEventDocument;
import org.apache.commons.lang3.math.NumberUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;

public class EventQueryDslBuilder {

    private final List<SpecSearchCriteria> params;

    public EventQueryDslBuilder() {
        params = new ArrayList<>();
    }


    public final EventQueryDslBuilder with(final String key,
                                           final String operation,
                                           final Object value,
                                           final String prefix,
                                           final String suffix) {
        return with(null,
                    key,
                    operation,
                    value,
                    prefix,
                    suffix);
    }

    public final EventQueryDslBuilder with(final String orPredicate,
                                           final String key,
                                           final String operation,
                                           final Object value,
                                           final String prefix,
                                           final String suffix) {
        SearchOperation op = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (op != null) {
            if (op == SearchOperation.EQUALITY) { // the operation may be complex operation
                final boolean startWithAsterisk = prefix != null && prefix.contains(SearchOperation.ZERO_OR_MORE_REGEX);
                final boolean endWithAsterisk = suffix != null && suffix.contains(SearchOperation.ZERO_OR_MORE_REGEX);

                if (startWithAsterisk && endWithAsterisk) {
                    op = SearchOperation.CONTAINS;
                } else if (startWithAsterisk) {
                    op = SearchOperation.ENDS_WITH;
                } else if (endWithAsterisk) {
                    op = SearchOperation.STARTS_WITH;
                }
            }
            params.add(new SpecSearchCriteria(orPredicate,
                                              key,
                                              op,
                                              value));
        }
        return this;
    }

    public Predicate build() {
        if (params.size() == 0) {
            return null;
        }
        BooleanBuilder builder = new BooleanBuilder();
        for (int i = 0; i < params.size(); i++) {
            if (params.get(i).isOrPredicate()) {
                builder.or(build(params.get(i)));
            } else {
                builder.and(build(params.get(i)));
            }
        }
        return builder;
    }

    private BooleanExpression build(SpecSearchCriteria criteria) {
        PathBuilder<AuditEventDocument> builder = new PathBuilder<AuditEventDocument>(AuditEventDocument.class, "document");
        if (criteria.getOperation().equals(SearchOperation.EQUALITY)) {
            return builder.get(criteria.getKey()).eq(criteria.getValue());
        } else if (criteria.getOperation().equals(SearchOperation.GREATER_THAN)) {
            return builder.getNumber(criteria.getKey(), Long.class).goe(NumberUtils.toLong(criteria.getValue()
                                                                                           .toString()));
        } else if (criteria.getOperation().equals(SearchOperation.LESS_THAN)) {
            return builder.getNumber(criteria.getKey(), Long.class).loe(NumberUtils.toLong(criteria.getValue()
                                                                                           .toString()));
        } else if (criteria.getOperation().equals(SearchOperation.NEGATION)) {
            return builder.get(criteria.getKey()).ne(criteria.getValue());
        } else if (criteria.getOperation().equals(SearchOperation.LIKE)) {
            return builder.getString(criteria.getKey()).like(criteria.getValue().toString());
        } else if (criteria.getOperation().equals(SearchOperation.CONTAINS)) {
            return builder.getString(criteria.getKey()).contains(criteria.getValue().toString());
        } else if (criteria.getOperation().equals(SearchOperation.STARTS_WITH)) {
            return builder.getString(criteria.getKey()).startsWith(criteria.getValue().toString());
        } else if (criteria.getOperation().equals(SearchOperation.ENDS_WITH)) {
            return builder.getString(criteria.getKey()).endsWith(criteria.getValue().toString());
        } else {
            throw new RuntimeException("未登録の型");
        }
    }

    public final EventQueryDslBuilder with(SpecSearchCriteria criteria) {
        params.add(criteria);
        return this;
    }

    public List<SpecSearchCriteria> getParams() {
        return params;
    }
}
