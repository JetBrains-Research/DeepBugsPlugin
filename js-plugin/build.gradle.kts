import org.jetbrains.intellij.tasks.*
import tanvd.kosogor.defaults.configureIdea

group = rootProject.group
version = rootProject.version

intellij {
    pluginName = "DeepBugs for JavaScript"
    version = rootProject.intellij.version
    type = "IU"
    downloadSources = true
    setPlugins("JavaScriptLanguage", "CSS", "platform-images")
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

    jvmArgs("-Xmx1g", "-Didea.is.internal=true")
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.withType<PatchPluginXmlTask> {
    sinceBuild("201")
    untilBuild("")
}

dependencies {
    implementation(project(":common"))
    implementation(project(":keras-runner"))
}
