package io.quarkus.test.keycloak;

import java.util.Collections;
import java.util.Map;

import com.tngtech.keycloakmock.api.KeycloakMock;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class KeycloakTestResource implements QuarkusTestResourceLifecycleManager {

    private KeycloakMock server;

    @Override
    public Map<String, String> start() {
        server = new KeycloakMock();
        server.start();
        System.out.println("[INFO] Keycloak started in mock mode");
        return Collections.emptyMap();
    }

    @Override
    public synchronized void stop() {
        if (server != null) {
            server.stop();
            System.out.println("[INFO] Keycloak has shut down");
            server = null;
        }
    }

}
