package com.jetbrains.deepbugs.settings

import com.intellij.openapi.components.*
import com.intellij.util.xmlb.XmlSerializerUtil

@State( name = "deepbugs_config", storages = [Storage("deepbugs_config.xml")])
class DeepBugsInspectionConfig : PersistentStateComponent<DeepBugsInspectionConfig>{

    override fun getState(): DeepBugsInspectionConfig = this

    override fun loadState(state: DeepBugsInspectionConfig) {
        XmlSerializerUtil.copyBean(state, this)
    }

    var threshold = 0.89

    companion object {
        fun getInstance(): DeepBugsInspectionConfig = ServiceManager.getService(DeepBugsInspectionConfig::class.java)
    }
}