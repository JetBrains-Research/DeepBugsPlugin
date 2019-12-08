import org.jetbrains.intellij.tasks.*
import tanvd.kosogor.defaults.configureIdea

group = rootProject.group
version = rootProject.version

intellij {
    pluginName = "DeepBugsJS"
    version = "2019.2"
    type = "IU"
    downloadSources = true
    setPlugins("JavaScriptLanguage", "CSS")
}

configureIdea {
    exclude += file("src/test/testData")
}

tasks.withType<PrepareSandboxTask> {
    from("${projectDir}/src/main/models") {
        into("${pluginName}/models")
    }
}

tasks.withType<RunIdeTask> {
    jvmArgs("-Xmx1g", "-Didea.is.internal=true")
}

tasks.withType<Test> {
    useJUnit()

    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.withType<PatchPluginXmlTask> {
    sinceBuild("192.5728")
    untilBuild("")
    changeNotes("Minor improvements. Usage statistics.")
}

dependencies {
    implementation(project(":common"))
    implementation(project(":keras-runner"))
}
