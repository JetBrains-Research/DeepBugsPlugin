package org.jetbrains.research.groups.ml_methods.deepbugs.services.logging

import com.google.gson.Gson
import org.jetbrains.annotations.NotNull
import org.jetbrains.research.groups.ml_methods.deepbugs.services.logging.events.LogData

object GsonUtils {
    val gson = Gson()
}

data class LogRecord(
        @NotNull var product: String,
        var userId: String,
        var build: String,
        var timestamp: Long,
        var sessionId: String,
        var recorderId: String,
        var recorderVersion: String,
        var bucket: String,
        var eventType: String,
        var event: LogData
) {
    constructor(
            product: String,
            userId: String,
            build: String,
            timestamp: Long,
            sessionId: String,
            recorderId: String,
            recorderVersion: String,
            eventType: EventType,
            event: LogData
    ) : this(product, userId, build, timestamp, sessionId, recorderId, recorderVersion, "-1", eventType.eventName, event)

    fun toTabString() =
                "$product\t" +
                "$userId\t" +
                "$build\t" +
                "$timestamp\t" +
                "$sessionId\t" +
                "$recorderId\t" +
                "$recorderVersion\t" +
                "$bucket\t" +
                "$eventType\t" +
                 GsonUtils.gson.toJson(event)
}