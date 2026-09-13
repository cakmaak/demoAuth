package com.demoSec.demoauth.authorization.service;

import com.demoSec.demoauth.authorization.dto.CreateDataClassRequest;
import com.demoSec.demoauth.authorization.entity.DataClass;
import com.demoSec.demoauth.authorization.repository.DataClassRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DataClassService {

    private final DataClassRepository dataClassRepository;

    public DataClassService(
            DataClassRepository dataClassRepository
    ) {
        this.dataClassRepository = dataClassRepository;
    }

    @Transactional
    public DataClass createDataClass(
            UUID tenantId,
            CreateDataClassRequest request
    ) {

        if (dataClassRepository
                .findByTenantIdAndCode(
                        tenantId,
                        request.getCode()
                )
                .isPresent()) {

            throw new IllegalStateException(
                    "Bu data class zaten mevcut: "
                            + request.getCode()
            );
        }

        DataClass dataClass = new DataClass();

        dataClass.setTenantId(tenantId);
        dataClass.setCode(request.getCode());
        dataClass.setName(request.getName());
        dataClass.setSensitivityRank(
                request.getSensitivityRank()
        );
        dataClass.setPolicyReference(
                request.getPolicyReference()
        );
        dataClass.setActive(
                request.isActive()
        );

        return dataClassRepository.save(dataClass);
    }
}