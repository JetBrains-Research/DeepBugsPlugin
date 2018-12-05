package org.jetbrains.research.groups.ml_methods.deepbugs.settings

import com.intellij.openapi.components.*
import com.intellij.util.xmlb.XmlSerializerUtil

@State( name = "deepbugs_config", storages = [Storage("deepbugs_config.xml")])
class DeepBugsInspectionConfig : PersistentStateComponent<DeepBugsInspectionConfig>{

    var curBinOperatorThreshold = DeepBugsInspectionConfigurable.defaultBinOperatorConfig
    var curBinOperandThreshold = DeepBugsInspectionConfigurable.defaultBinOperandConfig
    var curSwappedArgsThreshold = DeepBugsInspectionConfigurable.defaultSwappedArgsConfig

    override fun getState(): DeepBugsInspectionConfig = this

    override fun loadState(state: DeepBugsInspectionConfig) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        fun getInstance(): DeepBugsInspectionConfig = ServiceManager.getService(DeepBugsInspectionConfig::class.java)
    }
}