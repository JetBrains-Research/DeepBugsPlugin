package org.jetbrains.research.groups.ml_methods.deepbugs.downloader

interface DownloadProgress {
    var phase: String
    var name: String
    var progress: Double
}

object DownloadProgressProvider {
    var getProgress: () -> DownloadProgress = { SimpleDownloadProgress() }
}

class SimpleDownloadProgress(override var name: String = "", override var phase: String = "Download files", override var progress: Double = 0.0) : DownloadProgress

