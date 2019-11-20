package org.jetbrains.research.deepbugs.python.settings

import com.intellij.openapi.components.*
import com.intellij.util.xmlb.XmlSerializerUtil
import org.jetbrains.research.deepbugs.common.settings.DeepBugsInspectionConfig

@State(name = "DeepBugsPy", storages = [Storage("deepbugs.py.xml")])
class PyDeepBugsInspectionConfig : PersistentStateComponent<PyDeepBugsInspectionConfig>, DeepBugsInspectionConfig {
    override val configId: String = "PyInspectionConfig"

    override var curBinOperatorThreshold = PyDeepBugsInspectionConfigurable.PY_DEFAULT_BIN_OPERATOR_CONFIG
    override var curBinOperandThreshold = PyDeepBugsInspectionConfigurable.PY_DEFAULT_BIN_OPERAND_CONFIG
    override var curSwappedArgsThreshold = PyDeepBugsInspectionConfigurable.PY_DEFAULT_SWAPPED_ARGS_CONFIG

    override fun getState(): PyDeepBugsInspectionConfig = this

    override fun loadState(state: PyDeepBugsInspectionConfig) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        fun getInstance(): PyDeepBugsInspectionConfig = ServiceManager.getService(PyDeepBugsInspectionConfig::class.java)
    }
}
