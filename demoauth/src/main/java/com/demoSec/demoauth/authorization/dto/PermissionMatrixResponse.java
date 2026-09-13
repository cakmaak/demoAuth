package com.demoSec.demoauth.authorization.dto;

import java.util.UUID;

public class PermissionMatrixResponse {

    private String roleCode;
    private String roleName;

    private String resource;
    private String action;

    private boolean allowed;
    private boolean inherited;

    private UUID roleId;
    private UUID permissionId;
    private UUID grantId;
    private String permissionName;

    public PermissionMatrixResponse(
            UUID roleId,
            UUID permissionId,
            UUID grantId,
            String roleCode,
            String roleName,
            String resource,
            String action,
            boolean allowed,
            boolean inherited,
            String permissionName
    ) {
        this.roleId = roleId;
        this.permissionId = permissionId;
        this.grantId = grantId;
        this.roleCode = roleCode;
        this.roleName = roleName;
        this.resource = resource;
        this.action = action;
        this.allowed = allowed;
        this.inherited = inherited;
        this.permissionName = permissionName;
    }

    public UUID getRoleId() {
        return roleId;
    }

    public UUID getPermissionId() {
        return permissionId;
    }

    public UUID getGrantId() {
        return grantId;
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

    public boolean isAllowed() {
        return allowed;
    }

    public boolean isInherited() {
        return inherited;
    }

    public String getPermissionName() {
        return permissionName;
    }
}