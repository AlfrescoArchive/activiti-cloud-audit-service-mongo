package org.activiti.cloud.services.audit.mongo;

import org.activiti.cloud.services.audit.mongo.repository.EventsRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.github.fakemongo.Fongo;
import com.mongodb.MongoClient;

@Configuration
@EnableMongoRepositories(basePackageClasses = EventsRepository.class)
public class TestRepositoryConfig extends AbstractMongoConfiguration {

    @Override
    protected String getDatabaseName() {
        return "test";
    }

    @Override
    public MongoClient mongoClient() {
        return new Fongo("mongo-test").getMongo();
    }
}