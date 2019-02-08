package org.jetbrains.research.groups.ml_methods.deepbugs.services.log_reporter

import com.google.gson.Gson
import com.intellij.openapi.application.PermanentInstallationID

import java.util.UUID
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

class DeepBugsLogReporter {
    private val messages = LinkedBlockingQueue<String>()
    private val gson = Gson()

    init {
        val uploaderThread = Thread(Uploader(messages))
        uploaderThread.start()
    }

    fun <T: Any> log(sessionID: UUID, t: T, pluginName: String) {
        val builder = System.currentTimeMillis().toString() +
                "\t" +
                pluginName +
                "\t" +
                1 +
                "\t" +
                PermanentInstallationID.get() +
                "\t" +
                sessionID +
                "\t" +
                "-1" +
                "\t" +
                "report" +
                "\t" +
                gson.toJson(t, t::class.java)
        messages.offer(builder)
    }

    private inner class Uploader internal constructor(private val queue: BlockingQueue<String>) : Runnable {

        override fun run() {
            var errors = 0
            val maxErrors = 5

            while (true) {
                try {
                    if (errors > maxErrors) {
                        Thread.sleep((10000 * errors).toLong())
                    }

                    val message = queue.take()

                    if (!TestStatsSender.send(message)) {
                        errors++
                        queue.put(message)
                    } else {
                        errors = 0
                    }

                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }

        }
    }
}