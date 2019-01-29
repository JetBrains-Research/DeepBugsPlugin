package org.jetbrains.research.groups.ml_methods.deepbugs.services.downloader

data class PathUnit(
        val name: String,
        val url: String
)

data class Config(
        val name: String,
        val classpath: Set<PathUnit>
)