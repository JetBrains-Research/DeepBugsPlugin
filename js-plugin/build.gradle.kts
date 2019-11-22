import org.jetbrains.intellij.tasks.*

group = rootProject.group
version = rootProject.version

intellij {
    pluginName = "DeepBugsJS"
    version = "2019.2"
    type = "IU"
    downloadSources = true
    setPlugins("JavaScriptLanguage")
}

tasks.withType<PrepareSandboxTask> {
    from("${projectDir}/models") {
        into("${pluginName}/models")
    }
}

tasks.withType<RunIdeTask> {
    jvmArgs("-Xmx1g", "-Didea.is.internal=true")
}

tasks.withType<PatchPluginXmlTask> {
    sinceBuild("192.5728")
    untilBuild("")
    changeNotes("Minor improvements. Usage statistics.")
}

dependencies {
    implementation(project(":common"))
}
