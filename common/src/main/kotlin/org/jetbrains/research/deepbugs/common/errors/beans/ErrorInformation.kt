package org.jetbrains.research.deepbugs.common.errors.beans

data class ErrorInformation(
    val errorMessage: String?,
    val errorStacktrace: String?,
    val errorHash: String,
    val errorDescription: String?,
    var attachmentValue: String = "",
    var attachmentName: String = ""
)
