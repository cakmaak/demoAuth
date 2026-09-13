package com.demoSec.demoauth.gtfs.controller;

import com.demoSec.demoauth.authorization.annotation.Authorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(
        name = "GTFS",
        description = "Yetkilendirme korumalı GTFS veri erişimi"
)
@RestController
@RequestMapping("/api/gtfs")
public class GtfsController {


    @Operation(
            summary = "GTFS verisini görüntüle",
            description = "gtfs.data / VIEW permission'ı için authorization kontrolü yapar. Permission scope gerektiriyorsa istenen region kapsamında erişim doğrulanır."
    )
    @GetMapping
    @Authorize(
            resource = "gtfs.data",
            action = "VIEW"

    )
    public String getGtfsData(
            @Parameter(
                    description = "GTFS verisine erişilmek istenen region ID",
                    required = true
            )
            @RequestParam UUID regionId

    ) {

        return """
                GTFS verisine erişim başarılı.
                Authorization sonucu: ALLOW
                """;
    }
}