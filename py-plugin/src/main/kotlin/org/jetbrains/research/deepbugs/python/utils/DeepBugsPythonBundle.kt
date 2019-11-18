package org.jetbrains.research.deepbugs.python.utils

import com.intellij.CommonBundle
import com.intellij.reference.SoftReference
import org.jetbrains.annotations.PropertyKey
import java.lang.ref.Reference
import java.util.*

object DeepBugsPythonBundle {
    private const val BUNDLE = "DeepBugsPythonBundle"
    private var INSTANCE: Reference<ResourceBundle>? = null

    //FIXME-review do you really need all those soft references here?
    private val bundle: ResourceBundle?
        get() {
            var bundle = SoftReference.dereference(INSTANCE)
            if (bundle == null) {
                bundle = ResourceBundle.getBundle(BUNDLE)
                INSTANCE = SoftReference(bundle)
            }
            return bundle
        }

    fun message(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any): String {
        return CommonBundle.message(bundle!!, key, *params)
    }
}
