import { NavLink } from "react-router-dom";

function Layout({ children }) {

    const menuItems = [
        {
            path: "/",
            label: "Dashboard",
            icon: "◫"
        },
        {
            path: "/roles",
            label: "Roller",
            icon: "♙"
        },
        {
            path: "/permissions",
            label: "Yetkiler",
            icon: "⌁"
        },
        {
            path: "/principals",
            label: "Kullanıcılar",
            icon: "♧"
        },
        {
            path: "/permission-matrix",
            label: "Yetki Matrisi",
            icon: "▦"
        }
    ];

    return (
        <div className="app-shell">

            <aside className="sidebar">

                <div className="brand">
                    <div className="brand-icon">
                        D
                    </div>

                    <div>
                        <div className="brand-name">
                            DemoAuth
                        </div>

                        <div className="brand-subtitle">
                            Authorization Platform
                        </div>
                    </div>
                </div>

                <div className="sidebar-section-title">
                    YÖNETİM
                </div>

                <nav className="sidebar-nav">

                    {menuItems.map((item) => (

                        <NavLink
                            key={item.path}
                            to={item.path}
                            end={item.path === "/"}
                            className={({ isActive }) =>
                                isActive
                                    ? "nav-item active"
                                    : "nav-item"
                            }
                        >

                            <span className="nav-icon">
                                {item.icon}
                            </span>

                            <span>
                                {item.label}
                            </span>

                        </NavLink>

                    ))}

                </nav>

                <div className="sidebar-footer">

                    <div className="status-indicator">
                        <span className="status-dot" />

                        <div>
                            <div className="status-title">
                                Authorization Engine
                            </div>

                            <div className="status-text">
                                Sistem aktif
                            </div>
                        </div>
                    </div>

                </div>

            </aside>

            <div className="content-area">

                <header className="topbar">

                    <div>
                        <div className="environment-badge">
                            DEMO ENVIRONMENT
                        </div>
                    </div>

                    <div className="topbar-right">

                        <div className="engine-status">
                            <span className="status-dot" />
                            Casbin Active
                        </div>

                        <div className="user-avatar">
                            Y
                        </div>

                    </div>

                </header>

                <main className="main-content">
                    {children}
                </main>

            </div>

        </div>
    );
}

export default Layout;