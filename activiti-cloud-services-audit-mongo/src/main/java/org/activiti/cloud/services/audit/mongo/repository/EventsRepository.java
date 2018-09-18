package org.activiti.cloud.services.audit.mongo.repository;

import java.util.List;
import java.util.Optional;

import org.activiti.cloud.services.audit.mongo.events.AuditEventDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(exported = false)
public interface EventsRepository extends MongoRepository<AuditEventDocument, String>, QuerydslPredicateExecutor<AuditEventDocument> {

    Optional<AuditEventDocument> findByEventId(String eventId);

    @Override
    @RestResource(exported = false)
    public <S extends AuditEventDocument> S insert(S entity);

    @Override
    @RestResource(exported = false)
    public <S extends AuditEventDocument> List<S> insert(Iterable<S> entities);

    @Override
    @RestResource(exported = false)
    public <S extends AuditEventDocument> List<S> saveAll(Iterable<S> entities);

    @Override
    @RestResource(exported = false)
    public void delete(AuditEventDocument entity);

    @Override
    @RestResource(exported = false)
    public void deleteAll();

    @Override
    @RestResource(exported = false)
    public void deleteAll(Iterable<? extends AuditEventDocument> entities);

    @Override
    @RestResource(exported = false)
    public void deleteById(String id);

    @Override
    @RestResource(exported = false)
    public <S extends AuditEventDocument> S save(S entity);
}
