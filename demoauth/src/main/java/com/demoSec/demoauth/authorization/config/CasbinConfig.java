package com.demoSec.demoauth.authorization.config;

import org.casbin.jcasbin.main.Enforcer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Configuration
public class CasbinConfig {

    @Bean
    public Enforcer casbinEnforcer() throws IOException {

        ClassPathResource modelResource =
                new ClassPathResource("casbin/model.conf");

        ClassPathResource policyResource =
                new ClassPathResource("casbin/policy.csv");

        File modelFile = Files.createTempFile("casbin-model-", ".conf").toFile();
        File policyFile = Files.createTempFile("casbin-policy-", ".csv").toFile();

        Files.copy(
                modelResource.getInputStream(),
                modelFile.toPath(),
                java.nio.file.StandardCopyOption.REPLACE_EXISTING
        );

        Files.copy(
                policyResource.getInputStream(),
                policyFile.toPath(),
                java.nio.file.StandardCopyOption.REPLACE_EXISTING
        );

        return new Enforcer(
                modelFile.getAbsolutePath(),
                policyFile.getAbsolutePath()
        );
    }
}