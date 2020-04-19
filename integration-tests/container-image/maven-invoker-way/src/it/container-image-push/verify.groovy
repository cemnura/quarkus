import io.quarkus.deployment.util.ExecUtil

try {
    ExecUtil.exec("docker", "version", "--format", "'{{.Server.Version}}'")
} catch (Exception ignored) {
    println "Docker not found"
    return
}

try {
    ExecUtil.exec("curl", "--version")
} catch (Exception ignored) {
    println "curl not found"
    return
}

// TODO Validate returned JSON object
// {"repositories":["cemnura/container-image-push"]}
assert ExecUtil.exec("curl", "-X", "GET", "http://localhost:5000/v2/_catalog")

assert ExecUtil.exec("docker", "stop", "registry")