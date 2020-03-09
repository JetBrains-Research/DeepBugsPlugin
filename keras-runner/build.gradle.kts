group = "org.jetbrains.research"
version = rootProject.version

intellij {
    version = rootProject.intellij.version
}

dependencies {
    implementation("io.jhdf", "jhdf", "0.5.3") {
        // to avoid conflict with the dependency already included in IntelliJ Platform
        exclude("org.slf4j")
    }
    api("scientifik", "kmath-core-jvm", "0.1.3") {
        exclude("org.jetbrains.kotlin")
    }
}
