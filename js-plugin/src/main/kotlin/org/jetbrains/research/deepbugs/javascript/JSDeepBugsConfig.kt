package org.jetbrains.research.deepbugs.javascript

import com.intellij.openapi.components.*
import org.jetbrains.research.deepbugs.common.DeepBugsConfig
import org.jetbrains.research.deepbugs.common.datatypes.DataType

@State(name = "DeepBugsJS", storages = [Storage("deepbugs.js.xml")])
class JSDeepBugsConfig : PersistentStateComponent<DeepBugsConfig.State>, DeepBugsConfig(default) {
    companion object {
        private val instance by lazy { ServiceManager.getService(JSDeepBugsConfig::class.java) }

        val default = State(
            binOperatorThreshold = 0.8f,
            binOperandThreshold = 0.8f,
            swappedArgsThreshold = 0.8f
        )

        fun get() = instance.state

        @Synchronized
        fun ignoreExpression(expr: DataType) = instance.disableCheck(expr.text)

        @Synchronized
        fun considerExpression(expr: DataType) = instance.enableCheck(expr.text)

        fun shouldIgnore(expr: DataType) = get().userDisabledChecks.contains(expr.text)
    }
}
