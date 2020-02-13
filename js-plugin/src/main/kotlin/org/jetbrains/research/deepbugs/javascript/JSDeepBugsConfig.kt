package org.jetbrains.research.deepbugs.javascript

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import org.jetbrains.research.deepbugs.common.DeepBugsConfig
import org.jetbrains.research.deepbugs.common.DeepBugsConfigHandler

@State(name = "DeepBugsJS", storages = [Storage("deepbugs.js.xml")])
class JSDeepBugsConfig : PersistentStateComponent<DeepBugsConfig.State>, DeepBugsConfig(default) {
    companion object : DeepBugsConfigHandler() {
        override val instance: JSDeepBugsConfig by lazy { ServiceManager.getService(JSDeepBugsConfig::class.java) }
        override val default = State()
    }
}
