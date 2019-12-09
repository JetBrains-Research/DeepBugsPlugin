package org.jetbrains.research.deepbugs.python

import com.intellij.openapi.components.*
import org.jetbrains.research.deepbugs.common.DeepBugsConfig
import org.jetbrains.research.deepbugs.common.DeepBugsConfigHandler

@State(name = "DeepBugsPy", storages = [Storage("deepbugs.py.xml")])
class PyDeepBugsConfig : PersistentStateComponent<DeepBugsConfig.State>, DeepBugsConfig(default) {
    companion object : DeepBugsConfigHandler() {
        override val instance: PyDeepBugsConfig by lazy { ServiceManager.getService(PyDeepBugsConfig::class.java) }

        override val default = State(
            binOperatorThreshold = 0.8f,
            binOperandThreshold = 0.8f,
            swappedArgsThreshold = 0.8f
        )
    }
}
