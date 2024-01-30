package dev.njabulo.korturl;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "dev.njabulo.korturl.repository")
public class MongoDBConfig extends AbstractMongoClientConfiguration {

    private static final String DATABASE_NAME = "short-url-db";
    @Override
    protected String getDatabaseName() {
        return DATABASE_NAME;
    }
}
