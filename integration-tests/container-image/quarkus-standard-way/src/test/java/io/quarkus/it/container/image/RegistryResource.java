package io.quarkus.it.container.image;

import java.util.Collections;
import java.util.Map;

import io.quarkus.deployment.util.ExecUtil;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class RegistryResource implements QuarkusTestResourceLifecycleManager {

    @Override
    public Map<String, String> start() {
        ExecUtil.exec("docker", "run", "--rm", "-p", "5000:5000", "-d", "--name", "registry", "registry:2");
        System.setProperty("quarkus.container-image.registry", "localhost:5000");
        return Collections.emptyMap();
    }

    @Override
    public void stop() {
        System.clearProperty("quarkus.container-image.registry");
        ExecUtil.exec("docker", "stop", "registry");
    }
}
