import { useEffect, useState } from "react";
import keycloak from "../services/keycloak";

import {
    getRoles,
    createRole
} from "../services/authorizationService";

function Roles() {

    const [roles, setRoles] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [search, setSearch] = useState("");

    const [showCreateRole, setShowCreateRole] = useState(false);
    const [creating, setCreating] = useState(false);

    const [newRole, setNewRole] = useState({
        code: "",
        name: "",
        description: ""
    });

    useEffect(() => {
        loadRoles();
    }, []);

    const loadRoles = async () => {

        try {

            setError(null);

            const data = await getRoles(
                keycloak.token
            );

            console.log("Roles:", data);

            setRoles(data);

        } catch (error) {

            console.error(
                "Roller alınamadı:",
                error
            );

            setError(
                "Rol verileri alınamadı."
            );

        } finally {

            setLoading(false);

        }
    };

    const handleCreateRole = async (event) => {

        event.preventDefault();

        if (
            !newRole.code.trim() ||
            !newRole.name.trim()
        ) {
            alert(
                "Rol kodu ve rol adı zorunludur."
            );
            return;
        }

        try {

            setCreating(true);

            await createRole(
                keycloak.token,
                {
                    code: newRole.code
                        .trim()
                        .toUpperCase()
                        .replace(/\s+/g, "_"),

                    name: newRole.name.trim(),

                    description:
                        newRole.description.trim() ||
                        null
                }
            );

            setNewRole({
                code: "",
                name: "",
                description: ""
            });

            setShowCreateRole(false);

            await loadRoles();

        } catch (error) {

            console.error(
                "Rol oluşturulamadı:",
                error
            );

            alert(
                error.response?.data?.message ||
                "Rol oluşturulamadı."
            );

        } finally {

            setCreating(false);

        }
    };

    const filteredRoles = roles.filter(
        (role) => {

            const searchValue =
                search.toLowerCase();

            return (
                role.name
                    ?.toLowerCase()
                    .includes(searchValue) ||

                role.code
                    ?.toLowerCase()
                    .includes(searchValue) ||

                role.description
                    ?.toLowerCase()
                    .includes(searchValue)
            );
        }
    );

    const activeRoleCount =
        roles.filter(
            (role) => role.active
        ).length;

    const systemRoleCount =
        roles.filter(
            (role) => role.system
        ).length;

    if (loading) {

        return (
            <div className="matrix-state">

                <div className="loading-spinner" />

                <div>

                    <div className="state-title">
                        Roller yükleniyor
                    </div>

                    <div className="state-description">
                        Authorization rolleri alınıyor...
                    </div>

                </div>

            </div>
        );
    }

    if (error) {

        return (
            <div className="matrix-error">

                <strong>
                    Roller yüklenemedi
                </strong>

                <span>
                    {error}
                </span>

                <button
                    className="retry-button"
                    onClick={() => {
                        setLoading(true);
                        loadRoles();
                    }}
                >
                    Tekrar Dene
                </button>

            </div>
        );
    }

    return (

        <div className="roles-page">

            {/* =========================
                HEADER
            ========================== */}

            <div className="page-header">

                <div>

                    <h1>
                        Roller
                    </h1>

                    <p className="page-description">
                        Sistemde tanımlı authorization
                        rollerini görüntüleyin ve yönetin.
                    </p>

                </div>

                <div className="roles-header-actions">

                    <div className="authorization-status">

                        <span className="status-dot" />

                        {activeRoleCount} Aktif Rol

                    </div>

                    <button
                        type="button"
                        className="primary-button"
                        onClick={() =>
                            setShowCreateRole(true)
                        }
                    >

                        <span className="button-plus">
                            +
                        </span>

                        Yeni Rol

                    </button>

                </div>

            </div>

            {/* =========================
                STATS
            ========================== */}

            <div className="roles-stats">

                <div className="stat-card">

                    <div className="stat-label">
                        TOPLAM ROL
                    </div>

                    <div className="stat-value">
                        {roles.length}
                    </div>

                    <div className="stat-description">
                        Tanımlı rol
                    </div>

                </div>

                <div className="stat-card">

                    <div className="stat-label">
                        AKTİF ROL
                    </div>

                    <div className="stat-value">
                        {activeRoleCount}
                    </div>

                    <div className="stat-description">
                        Kullanılabilir rol
                    </div>

                </div>

                <div className="stat-card">

                    <div className="stat-label">
                        SİSTEM ROLÜ
                    </div>

                    <div className="stat-value">
                        {systemRoleCount}
                    </div>

                    <div className="stat-description">
                        Sistem tarafından tanımlı
                    </div>

                </div>

            </div>

            {/* =========================
                ROLE TABLE
            ========================== */}

            <div className="roles-card">

                <div className="roles-card-header">

                    <div>

                        <div className="matrix-card-title">
                            Rol Tanımları
                        </div>

                        <div className="matrix-card-description">
                            Authorization sistemindeki
                            mevcut roller.
                        </div>

                    </div>

                    <div className="role-search-wrapper">

                        <span className="search-icon">
                            ⌕
                        </span>

                        <input
                            type="text"
                            className="role-search"
                            placeholder="Rol ara..."
                            value={search}
                            onChange={(event) =>
                                setSearch(
                                    event.target.value
                                )
                            }
                        />

                    </div>

                </div>

                <div className="roles-table-wrapper">

                    <table className="roles-table">

                        <thead>

                            <tr>

                                <th>
                                    ROL
                                </th>

                                <th>
                                    KOD
                                </th>

                                <th>
                                    AÇIKLAMA
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

                            {filteredRoles.map(
                                (role) => (

                                    <tr key={role.id}>

                                        <td>

                                            <div className="role-main">

                                                <div className="role-avatar">
                                                    {
                                                        role.name
                                                            ?.charAt(0)
                                                            .toUpperCase()
                                                    }
                                                </div>

                                                <div>

                                                    <div className="role-table-name">
                                                        {role.name}
                                                    </div>

                                                    <div className="role-id">
                                                        {role.id}
                                                    </div>

                                                </div>

                                            </div>

                                        </td>

                                        <td>

                                            <span className="code-badge">
                                                {role.code}
                                            </span>

                                        </td>

                                        <td>

                                            <span className="role-description">
                                                {
                                                    role.description ||
                                                    "Açıklama bulunmuyor"
                                                }
                                            </span>

                                        </td>

                                        <td>

                                            {role.system ? (

                                                <span className="type-badge system">
                                                    Sistem
                                                </span>

                                            ) : (

                                                <span className="type-badge custom">
                                                    Özel
                                                </span>

                                            )}

                                        </td>

                                        <td>

                                            {role.active ? (

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

                    {filteredRoles.length === 0 && (

                        <div className="empty-state">

                            <div className="empty-title">
                                Rol bulunamadı
                            </div>

                            <div className="empty-description">
                                Arama kriterlerine uygun
                                bir rol bulunmuyor.
                            </div>

                        </div>

                    )}

                </div>

                <div className="matrix-footer">

                    <div>

                        <span className="footer-dot" />

                        Roller authorization
                        veritabanından yükleniyor.

                    </div>

                    <div>
                        {filteredRoles.length} rol gösteriliyor
                    </div>

                </div>

            </div>

            {/* =========================
                CREATE ROLE MODAL
            ========================== */}

            {showCreateRole && (

                <div
                    className="modal-overlay"
                    onMouseDown={() => {
                        if (!creating) {
                            setShowCreateRole(false);
                        }
                    }}
                >

                    <div
                        className="modal-card"
                        onMouseDown={(event) =>
                            event.stopPropagation()
                        }
                    >

                        {/* MODAL HEADER */}

                        <div className="modal-header">

                            <div>

                                <div className="modal-title">
                                    Yeni Rol Oluştur
                                </div>

                                <div className="modal-description">
                                    Authorization sistemi için
                                    yeni bir iş rolü tanımlayın.
                                </div>

                            </div>

                            <button
                                type="button"
                                className="modal-close"
                                disabled={creating}
                                onClick={() =>
                                    setShowCreateRole(false)
                                }
                            >
                                ×
                            </button>

                        </div>

                        {/* FORM */}

                        <form onSubmit={handleCreateRole}>

                            <div className="modal-body">

                                {/* ROLE NAME */}

                                <div className="form-group">

                                    <label>
                                        Rol Adı
                                    </label>

                                    <input
                                        type="text"
                                        className="form-input"
                                        placeholder="Örn. Çağrı Merkezi"
                                        value={newRole.name}
                                        onChange={(event) =>
                                            setNewRole({
                                                ...newRole,
                                                name: event.target.value
                                            })
                                        }
                                        autoFocus
                                    />

                                    <div className="form-hint">
                                        Kullanıcı arayüzünde
                                        gösterilecek rol adı.
                                    </div>

                                </div>

                                {/* ROLE CODE */}

                                <div className="form-group">

                                    <label>
                                        Rol Kodu
                                    </label>

                                    <input
                                        type="text"
                                        className="form-input code-input"
                                        placeholder="Örn. CALL_CENTER"
                                        value={newRole.code}
                                        onChange={(event) =>
                                            setNewRole({
                                                ...newRole,
                                                code: event.target.value
                                            })
                                        }
                                    />

                                    <div className="form-hint">
                                        Tenant içerisinde benzersiz
                                        teknik rol kodu.
                                    </div>

                                </div>

                                {/* DESCRIPTION */}

                                <div className="form-group">

                                    <label>

                                        Açıklama

                                        <span className="optional-label">
                                            Opsiyonel
                                        </span>

                                    </label>

                                    <textarea
                                        className="form-textarea"
                                        placeholder="Rolün kullanım amacını açıklayın..."
                                        value={newRole.description}
                                        onChange={(event) =>
                                            setNewRole({
                                                ...newRole,
                                                description: event.target.value
                                            })
                                        }
                                    />

                                </div>

                                {/* PREVIEW */}

                                <div className="role-preview">

                                    <div className="preview-label">
                                        ÖNİZLEME
                                    </div>

                                    <div className="preview-role">

                                        <div className="role-avatar">

                                            {
                                                newRole.name
                                                    ?.charAt(0)
                                                    .toUpperCase() ||
                                                "R"
                                            }

                                        </div>

                                        <div>

                                            <div className="role-table-name">

                                                {
                                                    newRole.name ||
                                                    "Yeni Rol"
                                                }

                                            </div>

                                            <div className="preview-code">

                                                {
                                                    newRole.code
                                                        ?.toUpperCase()
                                                        .replace(/\s+/g, "_") ||
                                                    "ROLE_CODE"
                                                }

                                            </div>

                                        </div>

                                    </div>

                                </div>

                            </div>

                            {/* MODAL FOOTER */}

                            <div className="modal-footer">

                                <button
                                    type="button"
                                    className="secondary-button"
                                    disabled={creating}
                                    onClick={() =>
                                        setShowCreateRole(false)
                                    }
                                >
                                    İptal
                                </button>

                                <button
                                    type="submit"
                                    className="primary-button"
                                    disabled={creating}
                                >

                                    {
                                        creating
                                            ? "Oluşturuluyor..."
                                            : "Rol Oluştur"
                                    }

                                </button>

                            </div>

                        </form>

                    </div>

                </div>

            )}

        </div>
    );
}

export default Roles;