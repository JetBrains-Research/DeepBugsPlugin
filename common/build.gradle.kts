group = rootProject.group
version = rootProject.version

repositories {
    mavenCentral()
    jcenter()
}

intellij {
    version = "2019.2"
}

dependencies {
    api("org.tensorflow", "tensorflow", "1.11.0")

    api("tanvd.kex", "kex", "0.1.1")

    implementation("org.eclipse.mylyn.github", "org.eclipse.egit.github.core", "2.1.5")
}
