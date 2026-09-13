import { Routes, Route } from "react-router-dom";
import Layout from "./components/Layout";

import Dashboard from "./pages/Dashboard";
import Roles from "./pages/Roles";
import Permissions from "./pages/Permissions";
import Principals from "./pages/Principals";
import PermissionMatrix from "./pages/PermissionMatrix";

function App() {
    return (
        <Layout>
            <Routes>
                <Route path="/" element={<Dashboard />} />
                <Route path="/roles" element={<Roles />} />
                <Route path="/permissions" element={<Permissions />} />
                <Route path="/principals" element={<Principals />} />
                <Route path="/permission-matrix" element={<PermissionMatrix />} />
            </Routes>
        </Layout>
    );
}

export default App;