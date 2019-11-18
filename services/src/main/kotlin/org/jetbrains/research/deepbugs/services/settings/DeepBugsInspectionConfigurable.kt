package org.jetbrains.research.deepbugs.services.settings

import com.intellij.openapi.options.Configurable

import org.jetbrains.research.deepbugs.services.logger.collectors.counter.SettingsStatsCollector
import org.jetbrains.research.deepbugs.services.ui.DeepBugsUI

import javax.swing.JComponent

abstract class DeepBugsInspectionConfigurable(protected val settings: DeepBugsInspectionConfig) : Configurable {
    private var deepBugsUI: DeepBugsUI? = null

    private fun logSettings() {
        return SettingsStatsCollector.logNewSettings(settings, deepBugsUI!!)
    }

    abstract fun createUI(): DeepBugsUI

    override fun getHelpTopic(): String? = null

    override fun isModified() =
        (deepBugsUI!!.binOperatorThreshold != settings.curBinOperatorThreshold) ||
            (deepBugsUI!!.binOperandThreshold != settings.curBinOperandThreshold) ||
            (deepBugsUI!!.swappedArgsThreshold != settings.curSwappedArgsThreshold)


    override fun apply() {
        logSettings()
        settings.curBinOperatorThreshold = deepBugsUI!!.binOperatorThreshold
        settings.curBinOperandThreshold = deepBugsUI!!.binOperandThreshold
        settings.curSwappedArgsThreshold = deepBugsUI!!.swappedArgsThreshold
    }

    override fun reset() {
        deepBugsUI!!.binOperatorThreshold = settings.curBinOperatorThreshold
        deepBugsUI!!.binOperandThreshold = settings.curBinOperandThreshold
        deepBugsUI!!.swappedArgsThreshold = settings.curSwappedArgsThreshold
    }

    override fun disposeUIResources() {
        deepBugsUI = null
    }

    override fun createComponent(): JComponent? {
        deepBugsUI = createUI()
        deepBugsUI!!.binOperatorThreshold = settings.curBinOperatorThreshold
        deepBugsUI!!.binOperandThreshold = settings.curBinOperandThreshold
        deepBugsUI!!.swappedArgsThreshold = settings.curSwappedArgsThreshold
        return deepBugsUI!!.rootPanel
    }
}
