group = rootProject.group
version = rootProject.version

intellij {
    version = rootProject.intellij.version
}

dependencies {
    implementation(project(":keras-runner"))
}
