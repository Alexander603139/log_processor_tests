package io.github.bugrov.log_processor_test.repository;

import io.github.bugrov.log_processor_test.entity.MongoLogDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoLogRepository extends MongoRepository<MongoLogDocument, String> {
}
