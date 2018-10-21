package org.jetbrains.research.groups.ml_methods.deepbugs.error_reporting

import com.intellij.errorreport.bean.ErrorBean
import java.util.Arrays

/**
 * Extends the standard class to provide the hash of the thrown exception stack trace.
 */
internal class GitHubErrorBean(throwable: Throwable, lastAction: String) : ErrorBean(throwable, lastAction) {

    val exceptionHash: String

    init {
        val hashCode = Integer.toUnsignedLong(Arrays.hashCode(throwable.stackTrace))
        exceptionHash = java.lang.Long.toHexString(hashCode)
    }
}