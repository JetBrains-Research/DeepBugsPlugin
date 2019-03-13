package org.jetbrains.research.groups.ml_methods.deepbugs.javascript.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import org.jetbrains.research.groups.ml_methods.deepbugs.services.settings.DeepBugsInspectionConfig

@State(name = "deepbugs_js_config", storages = [Storage("deepbugs_js_config.xml")])
class JSDeepBugsInspectionConfig : PersistentStateComponent<JSDeepBugsInspectionConfig>, DeepBugsInspectionConfig {
    override var curBinOperatorThreshold = JSDeepBugsInspectionConfigurable.JS_DEFAULT_BIN_OPERATOR_CONFIG
    override var curBinOperandThreshold = JSDeepBugsInspectionConfigurable.JS_DEFAULT_BIN_OPERAND_CONFIG
    override var curSwappedArgsThreshold = JSDeepBugsInspectionConfigurable.JS_DEFAULT_SWAPPED_ARGS_CONFIG

    override fun getState(): JSDeepBugsInspectionConfig = this

    override fun loadState(state: JSDeepBugsInspectionConfig) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        fun getInstance(): JSDeepBugsInspectionConfig = ServiceManager.getService(JSDeepBugsInspectionConfig::class.java)
    }
}