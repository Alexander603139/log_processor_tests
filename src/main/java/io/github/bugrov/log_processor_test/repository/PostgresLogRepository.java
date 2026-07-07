package io.github.bugrov.log_processor_test.repository;

import io.github.bugrov.log_processor_test.entity.PostgresLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostgresLogRepository extends JpaRepository<PostgresLogEntity, String> {
    boolean existsByIpAddress(String ipAddress);
}