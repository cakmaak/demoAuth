import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter } from "react-router-dom";

import App from "./App.jsx";
import keycloak from "./services/keycloak";

import "./index.css";

keycloak
    .init({
        onLoad: "login-required",
        checkLoginIframe: false,
    })
    .then((authenticated) => {

        if (!authenticated) {
            console.log("Kullanıcı giriş yapmadı.");
            return;
        }

        console.log("Keycloak login başarılı.");
        console.log("JWT:", keycloak.token);

        ReactDOM.createRoot(document.getElementById("root")).render(
            <React.StrictMode>
                <BrowserRouter>
                    <App />
                </BrowserRouter>
            </React.StrictMode>
        );
    })
    .catch((error) => {
        console.error("Keycloak başlatılamadı:", error);
    });