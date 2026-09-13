package com.demoSec.demoauth.authorization.dto;

public class RolePermissionGrantResponse {

    private String roleCode;
    private String roleName;

    private String resource;
    private String action;

    private String decision;
    private Integer priority;

    public RolePermissionGrantResponse(
            String roleCode,
            String roleName,
            String resource,
            String action,
            String decision,
            Integer priority
    ) {
        this.roleCode = roleCode;
        this.roleName = roleName;
        this.resource = resource;
        this.action = action;
        this.decision = decision;
        this.priority = priority;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getResource() {
        return resource;
    }

    public String getAction() {
        return action;
    }

    public String getDecision() {
        return decision;
    }

    public Integer getPriority() {
        return priority;
    }
}