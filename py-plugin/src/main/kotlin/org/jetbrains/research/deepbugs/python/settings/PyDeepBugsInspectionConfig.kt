package org.jetbrains.research.deepbugs.python.settings

import com.intellij.openapi.components.*
import com.intellij.openapi.project.ProjectManager
import org.jetbrains.research.deepbugs.common.msgbus.DeepBugsToolLifecycle
import org.jetbrains.research.deepbugs.common.settings.DeepBugsInspectionConfig
import org.jetbrains.research.deepbugs.common.settings.DeepBugsState

@State(name = "DeepBugsPy", storages = [Storage("deepbugs.py.xml")])
class PyDeepBugsInspectionConfig : PersistentStateComponent<DeepBugsState>, DeepBugsInspectionConfig {
    override val configId: String = "PyInspectionConfig"

    override var curState = DeepBugsState(
        curBinOperatorThreshold = PyDeepBugsInspectionConfigurable.PY_DEFAULT_BIN_OPERATOR_CONFIG,
        curBinOperandThreshold = PyDeepBugsInspectionConfigurable.PY_DEFAULT_BIN_OPERAND_CONFIG,
        curSwappedArgsThreshold = PyDeepBugsInspectionConfigurable.PY_DEFAULT_SWAPPED_ARGS_CONFIG
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
        private val instance by lazy { ServiceManager.getService(PyDeepBugsInspectionConfig::class.java) }

        fun getConfig() = instance.state
    }
}
