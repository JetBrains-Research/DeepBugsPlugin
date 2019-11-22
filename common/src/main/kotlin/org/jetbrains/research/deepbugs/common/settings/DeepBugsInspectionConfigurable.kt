package org.jetbrains.research.deepbugs.common.settings

import com.intellij.openapi.options.Configurable

import org.jetbrains.research.deepbugs.common.logger.collectors.counter.SettingsStatsCollector
import org.jetbrains.research.deepbugs.common.ui.DeepBugsUI

import javax.swing.JComponent

abstract class DeepBugsInspectionConfigurable(protected val settings: DeepBugsInspectionConfig) : Configurable {
    private lateinit var deepBugsUI: DeepBugsUI

    private fun logSettings() {
        return SettingsStatsCollector.logNewSettings(settings, deepBugsUI)
    }

    private fun getState() = DeepBugsState(
        curBinOperandThreshold = deepBugsUI.binOperandThreshold,
        curBinOperatorThreshold = deepBugsUI.binOperatorThreshold,
        curSwappedArgsThreshold = deepBugsUI.swappedArgsThreshold
    )

    abstract fun createUI(): DeepBugsUI

    override fun getHelpTopic(): String? = null

    override fun isModified() = getState() != settings.curState

    override fun apply() {
        logSettings()
        settings.update(getState())
    }

    override fun reset() {
        deepBugsUI.binOperatorThreshold = settings.curState.curBinOperatorThreshold
        deepBugsUI.binOperandThreshold = settings.curState.curBinOperandThreshold
        deepBugsUI.swappedArgsThreshold = settings.curState.curSwappedArgsThreshold
    }

    override fun disposeUIResources() {}

    override fun createComponent(): JComponent? {
        deepBugsUI = createUI()
        deepBugsUI.binOperatorThreshold = settings.curState.curBinOperatorThreshold
        deepBugsUI.binOperandThreshold = settings.curState.curBinOperandThreshold
        deepBugsUI.swappedArgsThreshold = settings.curState.curSwappedArgsThreshold
        return deepBugsUI.rootPanel
    }
}
