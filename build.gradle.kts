import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile

group = "org.jetbrains.research.groups.ml_methods.deepbugs"
version = "0.3"


plugins {
    id("tanvd.kosogor") version "1.0.7" apply true
    id("io.gitlab.arturbosch.detekt") version ("1.1.1") apply false
    id("org.jetbrains.intellij") version "0.4.13" apply false
    kotlin("jvm") version "1.3.31" apply true
}

allprojects {
    repositories {
        jcenter()
    }
}

subprojects {
    apply {
        plugin("kotlin")
        plugin("org.jetbrains.intellij")
        plugin("tanvd.kosogor")
        plugin("io.gitlab.arturbosch.detekt")
    }

    dependencies {
        compileOnly(kotlin("stdlib"))
        testCompile(kotlin("stdlib"))
    }

    tasks.withType<KotlinJvmCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            languageVersion = "1.3"
            apiVersion = "1.3"
        }
    }
}
