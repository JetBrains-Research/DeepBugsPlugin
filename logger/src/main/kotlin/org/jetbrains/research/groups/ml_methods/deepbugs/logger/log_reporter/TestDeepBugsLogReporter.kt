package org.jetbrains.research.groups.ml_methods.deepbugs.logger.log_reporter

import com.intellij.openapi.diagnostic.Logger
import com.intellij.util.io.HttpRequests

//simple test localhost logger
object TestDeepBugsLogReporter {
    private const val reportUrl = "http://localhost:3000/test/reported"
    private val LOG = Logger.getInstance(TestDeepBugsLogReporter::class.java)

    fun send(text: String): Boolean {
        try {
            executeRequest(text)
            return true
        } catch (e: Exception) {
            LOG.debug(e)
        }
        return false
    }

    private fun executeRequest(text: String) {
        HttpRequests.post(reportUrl, "application/json").write(text)
    }
}