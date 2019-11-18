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
    //FIXME-review Replace klaxon with Moshi or Jackson
    compile("com.beust:klaxon:3.0.1")
    compile("org.tensorflow:tensorflow:1.11.0")
    //FIXME-review Could be removed, replace with Kotlin-plain implementations
    compile("commons-io:commons-io:2.6")
}
