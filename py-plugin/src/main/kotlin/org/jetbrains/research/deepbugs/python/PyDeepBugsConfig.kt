package org.jetbrains.research.deepbugs.python

import com.intellij.openapi.components.*
import org.jetbrains.research.deepbugs.common.DeepBugsConfig
import org.jetbrains.research.deepbugs.common.datatypes.DataType

@State(name = "DeepBugsPy", storages = [Storage("deepbugs.py.xml")])
class PyDeepBugsConfig : PersistentStateComponent<DeepBugsConfig.State>, DeepBugsConfig(default) {
    companion object {
        private val instance by lazy { ServiceManager.getService(PyDeepBugsConfig::class.java) }

        val default = State(0.8f, 0.8f, 0.8f)

        fun get() = instance.state

        @Synchronized
        fun ignoreExpression(expr: DataType) = instance.disableCheck(expr.text)

        @Synchronized
        fun considerExpression(expr: DataType) = instance.enableCheck(expr.text)

        fun shouldIgnore(expr: DataType) = get().userDisabledChecks.contains(expr.text)
    }
}
