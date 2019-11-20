group = rootProject.group
version = rootProject.version

repositories {
    mavenCentral()
    jcenter()
}

intellij {
    //FIXME-review check that it is a minimal supported version
    version = "2019.2.2"
}

dependencies {
    api("org.tensorflow:tensorflow:1.11.0")

    implementation("org.eclipse.mylyn.github:org.eclipse.egit.github.core:2.1.5")

    implementation("com.squareup.moshi:moshi:1.9.1")
    implementation("com.squareup.moshi", "moshi-kotlin", "1.9.1")
}
