package org.jetbrains.research.groups.ml_methods.deepbugs.python.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(name = "deepbugs_python_config", storages = [Storage("deepbugs_python_config.xml")])
class PyDeepBugsInspectionConfig : PersistentStateComponent<PyDeepBugsInspectionConfig> {
    var curBinOperatorThreshold = PyDeepBugsInspectionConfigurable.DEFAULT_BIN_OPERATOR_CONFIG
    var curBinOperandThreshold = PyDeepBugsInspectionConfigurable.DEFAULT_BIN_OPERAND_CONFIG
    var curSwappedArgsThreshold = PyDeepBugsInspectionConfigurable.DEFAULT_SWAPPED_ARGS_CONFIG

    override fun getState(): PyDeepBugsInspectionConfig = this

    override fun loadState(state: PyDeepBugsInspectionConfig) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        fun getInstance(): PyDeepBugsInspectionConfig = ServiceManager.getService(PyDeepBugsInspectionConfig::class.java)
    }
}