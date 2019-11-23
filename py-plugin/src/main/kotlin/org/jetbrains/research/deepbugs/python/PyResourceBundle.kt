package org.jetbrains.research.deepbugs.python

import com.intellij.CommonBundle
import org.jetbrains.annotations.PropertyKey
import java.util.*

object PyResourceBundle {
    private const val BUNDLE_NAME = "DeepBugsPythonBundle"

    private val bundle by lazy { ResourceBundle.getBundle(BUNDLE_NAME) }

    fun message(@PropertyKey(resourceBundle = BUNDLE_NAME) key: String, vararg params: Any): String {
        return CommonBundle.message(bundle!!, key, *params)
    }
}
