package org.jetbrains.research.deepbugs.javascript

import com.intellij.openapi.components.*
import org.jetbrains.research.deepbugs.common.DeepBugsConfig
import org.jetbrains.research.deepbugs.common.DeepBugsConfigHandler

@State(name = "DeepBugsJS", storages = [Storage("deepbugs.js.xml")])
class JSDeepBugsConfig : PersistentStateComponent<DeepBugsConfig.State>, DeepBugsConfig(default) {
    companion object : DeepBugsConfigHandler() {
        override val instance: JSDeepBugsConfig by lazy { service<JSDeepBugsConfig>() }
        override val default = State()
    }
}
