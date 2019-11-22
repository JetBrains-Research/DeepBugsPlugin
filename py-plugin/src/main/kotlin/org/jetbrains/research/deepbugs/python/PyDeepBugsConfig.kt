package org.jetbrains.research.deepbugs.python

import com.intellij.openapi.components.*
import org.jetbrains.research.deepbugs.common.DeepBugsConfig

@State(name = "DeepBugsPy", storages = [Storage("deepbugs.py.xml")])
class PyDeepBugsConfig : PersistentStateComponent<DeepBugsConfig.State>, DeepBugsConfig("PyInspectionConfig", default) {
    companion object {
        private val instance by lazy { ServiceManager.getService(PyDeepBugsConfig::class.java) }

        val default = State(0.94f, 0.95f, 0.96f)

        fun get() = instance.state
    }
}
