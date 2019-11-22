package org.jetbrains.research.deepbugs.javascript

import com.intellij.openapi.components.*
import org.jetbrains.research.deepbugs.common.DeepBugsConfig

@State(name = "DeepBugsJS", storages = [Storage("deepbugs.js.xml")])
class JSDeepBugsConfig : PersistentStateComponent<DeepBugsConfig.State>, DeepBugsConfig("JSInspectionConfig", default) {
    companion object {
        private val instance by lazy { ServiceManager.getService(JSDeepBugsConfig::class.java) }

        val default = State(0.94f, 0.95f, 0.96f)

        fun get() = instance.state
    }
}
