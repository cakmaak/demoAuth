package com.demoSec.demoauth.authorization.controller;

import com.demoSec.demoauth.authorization.dto.CreateDataClassRequest;
import com.demoSec.demoauth.authorization.entity.DataClass;
import com.demoSec.demoauth.authorization.service.DataClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(
        name = "Data Classes",
        description = "Authorization veri sınıflandırmalarını yönetir"
)

@RestController
@RequestMapping("/api/admin/data-classes")
public class DataClassController {

    private final DataClassService dataClassService;

    public DataClassController(
            DataClassService dataClassService
    ) {
        this.dataClassService = dataClassService;
    }

    @Operation(
            summary = "Data class oluştur",
            description = "Authorization grant'larında kullanılabilecek yeni bir veri sınıflandırması oluşturur."
    )
    @PostMapping
    public DataClass createDataClass(
            @Parameter(description = "Data class'ın ait olduğu tenant ID", required = true)
            @RequestParam UUID tenantId,
            @Valid @RequestBody CreateDataClassRequest request
    ) {

        return dataClassService.createDataClass(
                tenantId,
                request
        );
    }
}