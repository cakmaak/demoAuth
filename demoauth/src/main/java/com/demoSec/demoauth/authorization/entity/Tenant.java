package com.demoSec.demoauth.authorization.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(
        name = "tenants",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_tenants_code",
                        columnNames = "code"
                )
        }
)
public class Tenant {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 100)
    private String code;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    public UUID getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}