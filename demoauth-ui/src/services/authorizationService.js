import axios from "axios";

const API_URL =
    import.meta.env.VITE_API_URL || "http://localhost:8080";

export const getMyPermissions = async (token) => {
    const response = await axios.get(
        `${API_URL}/api/authorization/me`,
        {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        }
    );

    return response.data;
};

export const getRoles = async (token) => {
    const response = await axios.get(
        `${API_URL}/api/admin/roles`,
        {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        }
    );

    return response.data;
};

export const getPermissions = async (token) => {
    const response = await axios.get(
        `${API_URL}/api/admin/permissions`,
        {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        }
    );

    return response.data;
};

export const getRolePermissionGrants = async (token) => {
    const response = await axios.get(
        `${API_URL}/api/admin/role-permission-grants`,
        {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        }
    );

    return response.data;
};
export const getPermissionMatrix = async (token) => {
    const response = await axios.get(
        `${API_URL}/api/admin/permission-matrix`,
        {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        }
    );

    return response.data;
};

export const createRolePermissionGrant = async (token, data) => {
    const response = await axios.post(
        `${API_URL}/api/admin/role-permission-grants`,
        data,
        {
            params: {
                tenantId: "6e973292-9dd3-467c-8b9d-dedc2169699e",
            },
            headers: {
                Authorization: `Bearer ${token}`,
            },
        }
    );

    return response.data;
};
export const deleteRolePermissionGrant = async (
    token,
    grantId
) => {

    await axios.delete(
        `${API_URL}/api/admin/role-permission-grants/${grantId}`,
        {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        }
    );
};

export const createRole = async (token, data) => {

    const response = await axios.post(
        `${API_URL}/api/admin/roles`,
        data,
        {
            params: {
                tenantId: "6e973292-9dd3-467c-8b9d-dedc2169699e"
            },
            headers: {
                Authorization: `Bearer ${token}`
            }
        }
    );

    return response.data;
};