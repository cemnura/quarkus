package io.quarkus.it.container.image;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItem;

import java.util.Collections;

import org.eclipse.microprofile.config.ConfigProvider;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.bootstrap.model.AppArtifact;
import io.quarkus.builder.Version;
import io.quarkus.test.QuarkusProdModeTest;
import io.quarkus.test.common.QuarkusTestResource;

@QuarkusTestResource(RegistryResource.class)
public class ContainerImagePushTest {

    @RegisterExtension
    static final QuarkusProdModeTest config = new QuarkusProdModeTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class).addClasses(GreetingResource.class))
            .setApplicationName("app")
            .setApplicationVersion("0.1-SNAPSHOT")
            .overrideConfigKey("quarkus.container-image.group", "localhost")
            .overrideConfigKey("quarkus.container-image.push", "true")
            .overrideConfigKey("quarkus.container-image.insecure", "true")
            .setForcedDependencies(
                    Collections.singletonList(
                            new AppArtifact("io.quarkus", "quarkus-container-image-jib", Version.getVersion())));

    @Test
    public void verifyContainerImageIsPushed() {
        String registryUri = ConfigProvider.getConfig().getValue("quarkus.container-image.registry", String.class);
        given()
                .when()
                .get("http://" + registryUri + "/v2/_catalog")
                .then()
                .body("repositories", hasItem("localhost/app"));
    }

}
