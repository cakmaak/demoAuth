import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
    url: import.meta.env.VITE_KEYCLOAK_URL || "http://localhost:8081",
    realm: "demo-realm",
    clientId: "demo-client",
});

export default keycloak;