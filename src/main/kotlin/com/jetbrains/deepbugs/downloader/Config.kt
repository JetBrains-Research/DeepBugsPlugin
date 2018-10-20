package com.jetbrains.deepbugs.downloader

data class PathUnit(
    val name: String,
    val url: String
)

data class Config(
    val name: String,
    val classpath: Set<PathUnit>
)