package org.jetbrains.research.deepbugs.python

import com.intellij.openapi.components.*
import org.jetbrains.research.deepbugs.common.DeepBugsConfig

@State(name = "DeepBugsPy", storages = [Storage("deepbugs.py.xml")])
class PyDeepBugsConfig : PersistentStateComponent<DeepBugsConfig.State>, DeepBugsConfig(default) {
    companion object {
        private val instance by lazy { ServiceManager.getService(PyDeepBugsConfig::class.java) }

        val default = State(0.8f, 0.8f, 0.8f)

        fun get() = instance.state

        @Synchronized
        fun ignoreExpression(expr: String) = instance.disableCheck(expr)

        @Synchronized
        fun considerExpression(expr: String) = instance.enableCheck(expr)

        fun shouldIgnore(expr: String) = get().userDisabledChecks.contains(expr)
    }
}
