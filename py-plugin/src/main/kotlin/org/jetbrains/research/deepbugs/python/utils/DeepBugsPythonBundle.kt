package org.jetbrains.research.deepbugs.python.utils

import com.intellij.CommonBundle
import com.intellij.reference.SoftReference
import org.jetbrains.annotations.PropertyKey
import org.jetbrains.research.deepbugs.services.utils.DeepBugsServicesBundle
import java.lang.ref.Reference
import java.util.*

object DeepBugsPythonBundle {
    private const val BUNDLE_NAME = "DeepBugsPythonBundle"

    private val bundle by lazy { ResourceBundle.getBundle(BUNDLE_NAME) }

    fun message(@PropertyKey(resourceBundle = BUNDLE_NAME) key: String, vararg params: Any): String {
        return CommonBundle.message(bundle!!, key, *params)
    }
}
