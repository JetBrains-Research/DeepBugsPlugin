group = rootProject.group
version = rootProject.version

dependencies {
    implementation(project(":keras-runner"))
    api("tanvd.kex", "kex", "0.1.1")
}
