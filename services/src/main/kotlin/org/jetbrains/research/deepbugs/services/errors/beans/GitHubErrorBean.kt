package org.jetbrains.research.deepbugs.services.errors.beans

import com.intellij.errorreport.bean.ErrorBean
import java.util.*

/**
 * Extends the standard class to provide the hash of the thrown exception stack trace.
 */
class GitHubErrorBean(throwable: Throwable, lastAction: String) : ErrorBean(throwable, lastAction) {

    val exceptionHash: String

    init {
        val hashCode = Integer.toUnsignedLong(Arrays.hashCode(throwable.stackTrace))
        exceptionHash = java.lang.Long.toHexString(hashCode)
    }
}
