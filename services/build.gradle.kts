group = rootProject.group
version = rootProject.version

repositories {
    mavenCentral()
    jcenter()
}

intellij {
    version = "2019.2.2"
}

dependencies {
    compile("org.eclipse.mylyn.github:org.eclipse.egit.github.core:2.1.5")
    compile("com.beust:klaxon:3.0.1")
    compile("org.tensorflow:tensorflow:1.11.0")
    compile("commons-io:commons-io:2.6")
}
