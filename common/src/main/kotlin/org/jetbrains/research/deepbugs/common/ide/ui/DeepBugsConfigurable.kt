package org.jetbrains.research.deepbugs.common.ide.ui

import com.intellij.openapi.options.Configurable
import org.jetbrains.research.deepbugs.common.DeepBugsConfig

import org.jetbrains.research.deepbugs.common.ide.fus.collectors.counter.SettingsStatsCollector
import javax.swing.JComponent

abstract class DeepBugsConfigurable(protected val settings: DeepBugsConfig) : Configurable {
    private val ui: DeepBugsUI by lazy { createUI() }

    private val state: DeepBugsConfig.State
        get() = DeepBugsConfig.State(
            binOperandThreshold = ui.binOperandThreshold,
            binOperatorThreshold = ui.binOperatorThreshold,
            swappedArgsThreshold = ui.swappedArgsThreshold
        )

    abstract fun createUI(): DeepBugsUI

    override fun getHelpTopic(): String? = null

    override fun isModified() = state != settings.state

    override fun apply() {
        SettingsStatsCollector.logNewSettings(settings, ui)
        settings.update(state)
    }

    override fun reset() {
        ui.binOperatorThreshold = settings.state.binOperatorThreshold
        ui.binOperandThreshold = settings.state.binOperandThreshold
        ui.swappedArgsThreshold = settings.state.swappedArgsThreshold
    }

    override fun createComponent(): JComponent? {
        ui.binOperatorThreshold = settings.state.binOperatorThreshold
        ui.binOperandThreshold = settings.state.binOperandThreshold
        ui.swappedArgsThreshold = settings.state.swappedArgsThreshold
        return ui.rootPanel
    }
}
