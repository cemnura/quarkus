package io.quarkus.test.keycloak;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import java.util.Collections;
import java.util.Map;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class KeycloakTestResource implements QuarkusTestResourceLifecycleManager {

    private WireMockServer server;

    @Override
    public Map<String, String> start() {

        server = new WireMockServer();
        server.start();

        WireMock.stubFor(
                get(urlEqualTo("/auth/realms/quarkus/.well-known/openid-configuration"))
                        .willReturn(aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withBody("{\n" +
                                        "    \"authorization_endpoint\": \"http://localhost:8080/authenticate\",\n" +
                                        "    \"end_session_endpoint\": \"http://localhost:8080/logout\",\n" +
                                        "    \"id_token_signing_alg_values_supported\": [\n" +
                                        "        \"RS256\",\n" +
                                        "        \"ES256\",\n" +
                                        "        \"HS256\"\n" +
                                        "    ],\n" +
                                        "    \"issuer\": \"http://localhost:8080/auth/realms/quarkus\",\n" +
                                        "    \"jwks_uri\": \"http://localhost:8080/auth/realms/quarkus/protocol/openid-connect/certs\",\n"
                                        +
                                        "    \"response_types_supported\": [\n" +
                                        "        \"code\",\n" +
                                        "        \"code id_token\",\n" +
                                        "        \"id_token\",\n" +
                                        "        \"token id_token\"\n" +
                                        "    ],\n" +
                                        "    \"subject_types_supported\": [\n" +
                                        "        \"public\"\n" +
                                        "    ],\n" +
                                        "    \"token_endpoint\": \"http://localhost:8080/auth/realms/quarkus/protocol/openid-connect/token\"\n"
                                        +
                                        "}")));

        WireMock.stubFor(
                get(urlEqualTo("/auth/realms/quarkus/protocol/openid-connect/certs"))
                        .willReturn(aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withBody("{\n" +
                                        "    \"keys\": [\n" +
                                        "        {\n" +
                                        "            \"alg\": \"RS256\",\n" +
                                        "            \"e\": \"AQAB\",\n" +
                                        "            \"kid\": \"keyId\",\n" +
                                        "            \"kty\": \"RSA\",\n" +
                                        "            \"n\": \"AKzaf4nijuwtAn9ieZaz-iGXBp1pFm6dJMAxRO6ax2CV9cBFeThxrKJNFmDY7j7gKRnrgWxvgJKSd3hAm_CGmXHbTM8cPi_gsof-CsOohv7LH0UYbr0UpCIJncTiRrKQto7q_NOO4Jh1EBSLMPX7MzttEhh35Ue9txHLq3zkdkR6BR6nGS7QxEg7FzYzA4IooV59OPr-TvlDxbEpwc1wkRZDGavo-WjngAt7m_BEQtHnav3whitbrMmi_1tWY8cQbO9D4FuQTM7yvACLSv94G2TCvsjm_gGJmOJyRBkI1r-uEIfhz9-VIKlswqapKSul-Hoxv5NycucRa4xi4N39dfM=\",\n"
                                        +
                                        "            \"use\": \"sig\"\n" +
                                        "        }\n" +
                                        "    ]\n" +
                                        "}")));

        System.out.println("[INFO] Keycloak started in mock mode");
        return Collections.singletonMap("quarkus.oidc.auth-server-url", server.baseUrl() + "/auth/realms/master");
    }

    @Override
    public synchronized void stop() {
        if (server != null) {
            server.stop();
            System.out.println("[INFO] Keycloak was shut down");
            server = null;
        }
    }

}
