import { useEffect, useState } from "react";
import keycloak from "../services/keycloak";

import {
    getPermissionMatrix,
    createRolePermissionGrant,
    deleteRolePermissionGrant
} from "../services/authorizationService";

function PermissionMatrix() {

    const [matrix, setMatrix] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Hangi permission üzerinde işlem yapıldığını tutuyoruz.
    const [updatingKey, setUpdatingKey] = useState(null);

    useEffect(() => {
        loadMatrix();
    }, []);

    const loadMatrix = async () => {

        try {

            setError(null);

            const data =
                await getPermissionMatrix(
                    keycloak.token
                );

            console.log(
                "Permission Matrix:",
                data
            );

            setMatrix(data);

        } catch (error) {

            console.error(
                "Permission matrix alınamadı:",
                error
            );

            setError(
                "Yetki matrisi verileri alınamadı."
            );

        } finally {

            setLoading(false);

        }
    };

    const handlePermissionChange = async (item) => {

        if (!item) {
            return;
        }

        if (item.inherited) {

            alert(
                "Bu yetki başka bir rolden miras geliyor."
            );

            return;
        }

        const itemKey =
            `${item.roleId}-${item.permissionId}`;

        try {

            setUpdatingKey(itemKey);

            if (item.allowed) {

                await deleteRolePermissionGrant(
                    keycloak.token,
                    item.grantId
                );

            } else {

                await createRolePermissionGrant(
                    keycloak.token,
                    {
                        roleId: item.roleId,
                        permissionId: item.permissionId,

                        dataClassId:
                            "997b9fad-189e-4be9-80e2-4547e5a659e6",

                        decision: "ALLOW",
                        priority: 100
                    }
                );
            }

            await loadMatrix();

        } catch (error) {

            console.error(
                "Yetki değiştirilemedi:",
                error
            );

            alert(
                "Yetki değiştirilemedi."
            );

        } finally {

            setUpdatingKey(null);

        }
    };

    if (loading) {

        return (
            <div className="matrix-state">
                <div className="loading-spinner" />

                <div>
                    <div className="state-title">
                        Yetki matrisi yükleniyor
                    </div>

                    <div className="state-description">
                        Roller ve permission bilgileri alınıyor...
                    </div>
                </div>
            </div>
        );
    }

    if (error) {

        return (
            <div className="matrix-error">
                <strong>
                    Yetki matrisi yüklenemedi
                </strong>

                <span>
                    {error}
                </span>

                <button
                    className="retry-button"
                    onClick={() => {
                        setLoading(true);
                        loadMatrix();
                    }}
                >
                    Tekrar Dene
                </button>
            </div>
        );
    }

    const roles = [
        ...new Map(
            matrix.map((item) => [
                item.roleCode,
                {
                    code: item.roleCode,
                    name: item.roleName
                }
            ])
        ).values()
    ];

    const permissions = [
        ...new Map(
            matrix.map((item) => [
                `${item.resource}-${item.action}`,
                {
                    name: item.permissionName,
                    resource: item.resource,
                    action: item.action
                }
            ])
        ).values()
    ];

    const directPermissionCount =
        matrix.filter(
            (item) =>
                item.allowed &&
                !item.inherited
        ).length;

    const inheritedPermissionCount =
        matrix.filter(
            (item) =>
                item.allowed &&
                item.inherited
        ).length;

    return (
        <div className="permission-matrix-page">

            {/* PAGE HEADER */}

            <div className="page-header">

                <div>
                    <h1>
                        Yetki Matrisi
                    </h1>

                    <p className="page-description">
                        Roller ile sistem yetkileri arasındaki
                        erişim ilişkilerini yönetin.
                    </p>
                </div>

                <div className="authorization-status">

                    <span className="status-dot" />

                    Authorization Active

                </div>

            </div>

            {/* STATS */}

            <div className="matrix-stats">

                <div className="stat-card">

                    <div className="stat-label">
                        TOPLAM ROL
                    </div>

                    <div className="stat-value">
                        {roles.length}
                    </div>

                    <div className="stat-description">
                        Aktif rol
                    </div>

                </div>

                <div className="stat-card">

                    <div className="stat-label">
                        TOPLAM YETKİ
                    </div>

                    <div className="stat-value">
                        {permissions.length}
                    </div>

                    <div className="stat-description">
                        Tanımlı permission
                    </div>

                </div>

                <div className="stat-card">

                    <div className="stat-label">
                        DOĞRUDAN ATAMA
                    </div>

                    <div className="stat-value">
                        {directPermissionCount}
                    </div>

                    <div className="stat-description">
                        Aktif grant
                    </div>

                </div>

                <div className="stat-card">

                    <div className="stat-label">
                        MİRAS YETKİ
                    </div>

                    <div className="stat-value">
                        {inheritedPermissionCount}
                    </div>

                    <div className="stat-description">
                        Role inheritance
                    </div>

                </div>

            </div>

            {/* MATRIX CARD */}

            <div className="matrix-card">

                <div className="matrix-card-header">

                    <div>

                        <div className="matrix-card-title">
                            Rol / Yetki Eşleşmeleri
                        </div>

                        <div className="matrix-card-description">
                            Bir role doğrudan yetki vermek veya
                            kaldırmak için ilgili kutuyu kullanın.
                        </div>

                    </div>

                    <div className="matrix-legend">

                        <div className="legend-item">
                            <span className="legend-box direct" />
                            Doğrudan
                        </div>

                        <div className="legend-item">
                            <span className="legend-box inherited" />
                            Miras
                        </div>

                    </div>

                </div>

                <div className="matrix-table-wrapper">

                    <table className="permission-table">

                        <thead>

                            <tr>

                                <th className="permission-column">
                                    YETKİ
                                </th>

                                {roles.map((role) => (

                                    <th
                                        key={role.code}
                                        className="role-column"
                                    >

                                        <div className="role-name">
                                            {role.name}
                                        </div>

                                        <div className="role-code">
                                            {role.code}
                                        </div>

                                    </th>

                                ))}

                            </tr>

                        </thead>

                        <tbody>

                            {permissions.map(
                                (permission) => (

                                    <tr
                                        key={
                                            `${permission.resource}-${permission.action}`
                                        }
                                    >

                                        <td className="permission-info">

                                            <div className="permission-name">
                                                {permission.name}
                                            </div>

                                            <div className="permission-technical">
                                                {permission.resource}

                                                <span className="technical-separator">
                                                    ·
                                                </span>

                                                {permission.action}
                                            </div>

                                        </td>

                                        {roles.map((role) => {

                                            const item =
                                                matrix.find(
                                                    (entry) =>
                                                        entry.roleCode === role.code &&
                                                        entry.resource === permission.resource &&
                                                        entry.action === permission.action
                                                );

                                            const itemKey =
                                                item
                                                    ? `${item.roleId}-${item.permissionId}`
                                                    : null;

                                            const isUpdating =
                                                updatingKey === itemKey;

                                            return (

                                                <td
                                                    key={role.code}
                                                    className="permission-cell"
                                                >

                                                    <label
                                                        className={
                                                            item?.inherited
                                                                ? "permission-checkbox inherited"
                                                                : "permission-checkbox"
                                                        }
                                                    >

                                                        <input
                                                            type="checkbox"
                                                            checked={
                                                                item?.allowed ||
                                                                false
                                                            }
                                                            disabled={
                                                                item?.inherited ||
                                                                isUpdating
                                                            }
                                                            onChange={() =>
                                                                handlePermissionChange(
                                                                    item
                                                                )
                                                            }
                                                        />

                                                        <span className="custom-checkbox">

                                                            {isUpdating
                                                                ? "···"
                                                                : item?.allowed
                                                                    ? "✓"
                                                                    : ""
                                                            }

                                                        </span>

                                                    </label>

                                                    {item?.inherited && (

                                                        <div className="inheritance-label">
                                                            Miras
                                                        </div>

                                                    )}

                                                </td>

                                            );

                                        })}

                                    </tr>

                                )
                            )}

                        </tbody>

                    </table>

                </div>

                {/* FOOTER */}

                <div className="matrix-footer">

                    <div>
                        <span className="footer-dot" />

                        Değişiklikler doğrudan
                        authorization veritabanına uygulanır.
                    </div>

                    <div>
                        {roles.length} rol · {permissions.length} yetki
                    </div>

                </div>

            </div>

        </div>
    );
}

export default PermissionMatrix;