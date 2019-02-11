package org.jetbrains.research.groups.ml_methods.deepbugs.javascript.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(name = "deepbugs_js_config", storages = [Storage("deepbugs_js_config.xml")])
class JSDeepBugsInspectionConfig : PersistentStateComponent<JSDeepBugsInspectionConfig> {
    var curBinOperatorThreshold = JSDeepBugsInspectionConfigurable.DEFAULT_BIN_OPERATOR_CONFIG
    var curBinOperandThreshold = JSDeepBugsInspectionConfigurable.DEFAULT_BIN_OPERAND_CONFIG
    var curSwappedArgsThreshold = JSDeepBugsInspectionConfigurable.DEFAULT_SWAPPED_ARGS_CONFIG

    override fun getState(): JSDeepBugsInspectionConfig = this

    override fun loadState(state: JSDeepBugsInspectionConfig) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        fun getInstance(): JSDeepBugsInspectionConfig = ServiceManager.getService(JSDeepBugsInspectionConfig::class.java)
    }
}