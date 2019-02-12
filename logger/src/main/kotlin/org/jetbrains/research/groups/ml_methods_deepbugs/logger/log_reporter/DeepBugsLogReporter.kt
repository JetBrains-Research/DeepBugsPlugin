package org.jetbrains.research.groups.ml_methods_deepbugs.logger.log_reporter

import com.google.common.net.HttpHeaders
import com.google.gson.Gson
import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.application.ApplicationNamesInfo
import com.intellij.openapi.application.PermanentInstallationID
import com.intellij.openapi.diagnostic.Logger
import com.intellij.util.io.HttpRequests
import org.apache.commons.codec.binary.Base64OutputStream
import org.jetbrains.research.groups.ml_methods_deepbugs.logger.logging.EventType
import org.jetbrains.research.groups.ml_methods_deepbugs.logger.logging.LogRecord
import org.jetbrains.research.groups.ml_methods_deepbugs.logger.logging.events.LogData
import java.io.ByteArrayOutputStream
import java.util.zip.GZIPOutputStream

private class StatsServerInfo(
        @JvmField var status: String,
        @JvmField var url: String,
        @JvmField var urlForZipBase64Content: String
) {
    fun isServiceAlive() = "ok" == status
}

private val gson by lazy { Gson() }

object DeepBugsLogReporter {
    private const val infoUrl = "https://www.jetbrains.com/config/features-service-status.json"
    private val LOG = Logger.getInstance(DeepBugsLogReporter::class.java)

    private fun requestServerUrl(): StatsServerInfo? {
        try {
            val info = gson.fromJson(HttpRequests.request(infoUrl).readString(), StatsServerInfo::class.java)
            if (info.isServiceAlive()) return info
        }
        catch (e: Exception) {
            LOG.debug(e)
        }
        return null
    }

    private fun executeRequest(info: StatsServerInfo, text: String, compress: Boolean) {
        if (compress) {
            val data = Base64GzipCompressor.compress(text)
            HttpRequests
                    .post(info.urlForZipBase64Content, null)
                    .tuner { it.setRequestProperty(HttpHeaders.CONTENT_ENCODING, "gzip") }
                    .write(data)
            return
        }

        HttpRequests.post(info.url, "text/html").write(text)
    }

    fun send(text: String, compress: Boolean = true): Boolean {
        val info = requestServerUrl() ?: return false
        try {
            executeRequest(info, text, compress)
            return true
        }
        catch (e: Exception) {
            LOG.debug(e)
        }
        return false
    }
}

private object Base64GzipCompressor {
    fun compress(text: String): ByteArray {
        val outputStream = ByteArrayOutputStream()
        val base64Stream = GZIPOutputStream(Base64OutputStream(outputStream))
        base64Stream.write(text.toByteArray())
        base64Stream.close()
        return outputStream.toByteArray()
    }
}

fun createLogRecord(sessionId: String, recorderId: String, recorderVersion: String, eventType: EventType, logData: LogData): String {
    val timestamp = System.currentTimeMillis()
    val userId = PermanentInstallationID.get()
    val product = ApplicationNamesInfo.getInstance().productName
    val build = ApplicationInfo.getInstance().build.asStringWithoutProductCodeAndSnapshot()
    val log = LogRecord(
            product = product,
            userId = userId,
            build = build,
            timestamp = timestamp,
            sessionId = sessionId,
            recorderId = recorderId,
            recorderVersion = recorderVersion,
            eventType = eventType,
            event = logData)
    return log.toTabString()
}