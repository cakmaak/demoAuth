import { useEffect, useState } from "react";
import keycloak from "../services/keycloak";
import { getPermissions } from "../services/authorizationService";

function Permissions() {

    const [permissions, setPermissions] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [search, setSearch] = useState("");

    useEffect(() => {
        loadPermissions();
    }, []);

    const loadPermissions = async () => {

        try {

            setError(null);

            const data =
                await getPermissions(
                    keycloak.token
                );

            console.log(
                "Permissions:",
                data
            );

            setPermissions(data);

        } catch (error) {

            console.error(
                "Permission verileri alınamadı:",
                error
            );

            setError(
                "Yetki verileri alınamadı."
            );

        } finally {

            setLoading(false);

        }
    };

    const filteredPermissions =
        permissions.filter((permission) => {

            const searchValue =
                search.toLowerCase();

            return (
                permission.name
                    ?.toLowerCase()
                    .includes(searchValue) ||

                permission.resourceKey
                    ?.toLowerCase()
                    .includes(searchValue) ||

                permission.actionKey
                    ?.toLowerCase()
                    .includes(searchValue)
            );
        });

    const activePermissionCount =
        permissions.filter(
            (permission) => permission.active
        ).length;

    const scopedPermissionCount =
        permissions.filter(
            (permission) => permission.scopeRequired
        ).length;

    const resourceCount =
        new Set(
            permissions.map(
                (permission) =>
                    permission.resourceKey
            )
        ).size;

    if (loading) {

        return (
            <div className="matrix-state">

                <div className="loading-spinner" />

                <div>

                    <div className="state-title">
                        Yetkiler yükleniyor
                    </div>

                    <div className="state-description">
                        Permission tanımları alınıyor...
                    </div>

                </div>

            </div>
        );
    }

    if (error) {

        return (
            <div className="matrix-error">

                <strong>
                    Yetkiler yüklenemedi
                </strong>

                <span>
                    {error}
                </span>

                <button
                    className="retry-button"
                    onClick={() => {
                        setLoading(true);
                        loadPermissions();
                    }}
                >
                    Tekrar Dene
                </button>

            </div>
        );
    }

    return (

        <div className="permissions-page">

            {/* HEADER */}

            <div className="page-header">

                <div>

                    <h1>
                        Yetkiler
                    </h1>

                    <p className="page-description">
                        Sistemde tanımlı resource ve action
                        bazlı erişim yetkilerini görüntüleyin.
                    </p>

                </div>

                <div className="authorization-status">

                    <span className="status-dot" />

                    {activePermissionCount} Aktif Yetki

                </div>

            </div>

            {/* STATS */}

            <div className="permissions-stats">

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
                        AKTİF YETKİ
                    </div>

                    <div className="stat-value">
                        {activePermissionCount}
                    </div>

                    <div className="stat-description">
                        Kullanılabilir permission
                    </div>

                </div>

                <div className="stat-card">

                    <div className="stat-label">
                        RESOURCE
                    </div>

                    <div className="stat-value">
                        {resourceCount}
                    </div>

                    <div className="stat-description">
                        Benzersiz kaynak
                    </div>

                </div>

                <div className="stat-card">

                    <div className="stat-label">
                        SCOPE GEREKTİREN
                    </div>

                    <div className="stat-value">
                        {scopedPermissionCount}
                    </div>

                    <div className="stat-description">
                        Kapsam kontrollü yetki
                    </div>

                </div>

            </div>

            {/* TABLE */}

            <div className="permissions-card">

                <div className="permissions-card-header">

                    <div>

                        <div className="matrix-card-title">
                            Permission Tanımları
                        </div>

                        <div className="matrix-card-description">
                            Resource / Action tabanlı
                            authorization yetkileri.
                        </div>

                    </div>

                    <div className="role-search-wrapper">

                        <span className="search-icon">
                            ⌕
                        </span>

                        <input
                            type="text"
                            className="role-search"
                            placeholder="Yetki ara..."
                            value={search}
                            onChange={(event) =>
                                setSearch(
                                    event.target.value
                                )
                            }
                        />

                    </div>

                </div>

                <div className="permissions-table-wrapper">

                    <table className="permissions-table">

                        <thead>

                            <tr>

                                <th>
                                    YETKİ
                                </th>

                                <th>
                                    RESOURCE
                                </th>

                                <th>
                                    ACTION
                                </th>

                                <th>
                                    SCOPE
                                </th>

                                <th>
                                    TÜR
                                </th>

                                <th>
                                    DURUM
                                </th>

                            </tr>

                        </thead>

                        <tbody>

                            {filteredPermissions.map(
                                (permission) => (

                                    <tr key={permission.id}>

                                        {/* NAME */}

                                        <td>

                                            <div className="permission-list-main">

                                                <div className="permission-list-icon">
                                                    P
                                                </div>

                                                <div>

                                                    <div className="permission-list-name">
                                                        {permission.name}
                                                    </div>

                                                    <div className="permission-list-id">
                                                        {permission.id}
                                                    </div>

                                                </div>

                                            </div>

                                        </td>

                                        {/* RESOURCE */}

                                        <td>

                                            <span className="resource-badge">
                                                {permission.resourceKey}
                                            </span>

                                        </td>

                                        {/* ACTION */}

                                        <td>

                                            <span
                                                className={
                                                    `action-badge action-${permission.actionKey?.toLowerCase()}`
                                                }
                                            >
                                                {permission.actionKey}
                                            </span>

                                        </td>

                                        {/* SCOPE */}

                                        <td>

                                            {permission.scopeRequired ? (

                                                <span className="scope-badge required">
                                                    Scope Gerekli
                                                </span>

                                            ) : (

                                                <span className="scope-badge none">
                                                    Global
                                                </span>

                                            )}

                                        </td>

                                        {/* SYSTEM */}

                                        <td>

                                            {permission.system ? (

                                                <span className="type-badge system">
                                                    Sistem
                                                </span>

                                            ) : (

                                                <span className="type-badge custom">
                                                    Özel
                                                </span>

                                            )}

                                        </td>

                                        {/* STATUS */}

                                        <td>

                                            {permission.active ? (

                                                <span className="role-status active">

                                                    <span className="role-status-dot" />

                                                    Aktif

                                                </span>

                                            ) : (

                                                <span className="role-status passive">

                                                    <span className="role-status-dot" />

                                                    Pasif

                                                </span>

                                            )}

                                        </td>

                                    </tr>

                                )
                            )}

                        </tbody>

                    </table>

                    {filteredPermissions.length === 0 && (

                        <div className="empty-state">

                            <div className="empty-title">
                                Yetki bulunamadı
                            </div>

                            <div className="empty-description">
                                Arama kriterlerine uygun
                                permission bulunmuyor.
                            </div>

                        </div>

                    )}

                </div>

                <div className="matrix-footer">

                    <div>

                        <span className="footer-dot" />

                        Yetkiler authorization
                        veritabanından yükleniyor.

                    </div>

                    <div>
                        {filteredPermissions.length} yetki gösteriliyor
                    </div>

                </div>

            </div>

        </div>
    );
}

export default Permissions;