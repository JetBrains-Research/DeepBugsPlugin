group = rootProject.group
version = rootProject.version

dependencies {
    api("io.jhdf:jhdf:0.5.3") {
        // to avoid conflict with the dependency already included in IntelliJ Platform
        exclude("org.slf4j")
    }
    api("scientifik:kmath-core-jvm:0.1.3")
}
