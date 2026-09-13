package com.demoSec.demoauth.authorization.service;

import org.casbin.jcasbin.main.Enforcer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CasbinService {

    private final Enforcer enforcer;

    public CasbinService(Enforcer enforcer) {
        this.enforcer = enforcer;
    }

    public boolean checkPermission(
            String subject,
            String resource,
            String action
    ) {
        return enforcer.enforce(subject, resource, action);
    }
    public List<List<String>> getPolicies() {
        return enforcer.getPolicy();
    }
}