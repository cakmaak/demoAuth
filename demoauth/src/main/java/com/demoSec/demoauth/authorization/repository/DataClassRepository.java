package com.demoSec.demoauth.authorization.repository;

import com.demoSec.demoauth.authorization.entity.DataClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DataClassRepository extends JpaRepository<DataClass, UUID> {

    Optional<DataClass> findByTenantIdAndCode(
            UUID tenantId,
            String code
    );
}