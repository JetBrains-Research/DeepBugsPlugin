package org.jetbrains.research.groups.ml_methods.deepbugs.downloader

import com.intellij.openapi.progress.ProgressIndicator

interface DownloadProgress {
    var phase: String
    var name: String
    var progress: Double
}

class DownloadProgressWrapper(private val progressIndicator: ProgressIndicator) : DownloadProgress {

    override var progress: Double = 0.0
        set(value) {
            field = value
            progressIndicator.fraction = progress
        }

    override var name: String = ""
        set(value) {
            field = value
            progressIndicator.text = name
        }

    override var phase: String = ""
        set(value) {
            field = value
            progressIndicator.text = phase
        }
}

object DownloadProgressProvider {
    var getProgress: () -> DownloadProgress = { SimpleDownloadProgress() }
}

class SimpleDownloadProgress(override var name: String = "", override var phase: String = "Download files",
        override var progress: Double = 0.0) : DownloadProgress

