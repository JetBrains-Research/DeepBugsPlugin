package org.jetbrains.research.deepbugs.common.errors.beans

import com.intellij.errorreport.bean.ErrorBean
import java.util.*

/**
 * Extends the standard class to provide the hash of the thrown exception stack trace.
 */
//FIXME-review here is used deprecated ErrorBean
class GitHubErrorBean(throwable: Throwable, lastAction: String) : ErrorBean(throwable, lastAction) {
    //FIXME-review cannot it be replaced with simpler hashcode?
    val exceptionHash: String = java.lang.Long.toHexString(Integer.toUnsignedLong(Arrays.hashCode(throwable.stackTrace)))
}
