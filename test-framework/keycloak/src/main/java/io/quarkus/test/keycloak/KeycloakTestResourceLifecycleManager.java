package io.quarkus.test.keycloak;

import static com.tngtech.keycloakmock.api.ServerConfig.aServerConfig;

import java.util.Collections;
import java.util.Map;

import com.tngtech.keycloakmock.api.KeycloakMock;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class KeycloakTestResourceLifecycleManager implements QuarkusTestResourceLifecycleManager {

    private KeycloakMock mock;

    @Override
    public Map<String, String> start() {
        mock = new KeycloakMock(aServerConfig()
                .withPort(8000).withRealm("quarkus").build());
        System.out.println("[INFO] Keycloak started in mock mode");
        mock.start();
        return Collections.emptyMap();
    }

    @Override
    public synchronized void stop() {
        if (mock != null) {
            System.out.println("[INFO] Keycloak was shut down");
            mock.stop();
        }
    }
}
