package org.jetbrains.research.deepbugs.javascript.settings

import com.intellij.openapi.components.*
import com.intellij.openapi.project.ProjectManager
import org.jetbrains.research.deepbugs.common.msgbus.DeepBugsToolLifecycle
import org.jetbrains.research.deepbugs.common.settings.DeepBugsInspectionConfig
import org.jetbrains.research.deepbugs.common.settings.DeepBugsState

@State(name = "DeepBugsJS", storages = [Storage("deepbugs.js.xml")])
class JSDeepBugsInspectionConfig : PersistentStateComponent<DeepBugsState>, DeepBugsInspectionConfig {

    override val configId: String = "JSInspectionConfig"

    override var curState: DeepBugsState = DeepBugsState(
            curBinOperatorThreshold = JSDeepBugsInspectionConfigurable.JS_DEFAULT_BIN_OPERATOR_CONFIG,
            curBinOperandThreshold = JSDeepBugsInspectionConfigurable.JS_DEFAULT_BIN_OPERAND_CONFIG,
            curSwappedArgsThreshold = JSDeepBugsInspectionConfigurable.JS_DEFAULT_SWAPPED_ARGS_CONFIG
        )

    override fun getState(): DeepBugsState = curState

    override fun loadState(state: DeepBugsState) {
        val prevState = curState
        curState = state

        ProjectManager.getInstance().openProjects.forEach {
            DeepBugsToolLifecycle.publisher.updateState(prevState, curState, it)
        }
    }

    companion object {
        private val instance by lazy { ServiceManager.getService(JSDeepBugsInspectionConfig::class.java) }

        fun getConfig() = instance.state
    }
}
