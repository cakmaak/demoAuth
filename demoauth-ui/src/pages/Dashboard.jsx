import { useEffect, useState } from "react";
import keycloak from "../services/keycloak";
import { getMyPermissions } from "../services/authorizationService";

function Dashboard() {
    
     console.log("DASHBOARD COMPONENT ÇALIŞTI");

    const [permissions, setPermissions] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {

        const loadPermissions = async () => {

            try {

                const data = await getMyPermissions(keycloak.token);
                console.log("Gelen yetkiler:", data);

                setPermissions(data);

            } catch (error) {

                console.error("Yetkiler alınamadı:", error);

            } finally {

                setLoading(false);

            }
        };

        loadPermissions();

    }, []);

    if (loading) {
        return <h1>Yetkiler yükleniyor...</h1>;
    }

    return (
        <div>

            <h1>Dashboard</h1>

            <h2>Benim Yetkilerim</h2>

            {permissions.length === 0 ? (

                <p>Herhangi bir yetkiniz bulunmuyor.</p>

            ) : (

                <ul>
                    {permissions.map((permission, index) => (
                        <li key={index}>
                            {permission.resource} → {permission.action}
                        </li>
                    ))}
                </ul>

            )}
            

        </div>
    );
    
}


export default Dashboard;