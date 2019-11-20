package org.jetbrains.research.deepbugs.services.settings

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.ProjectManager

import org.jetbrains.research.deepbugs.services.logger.collectors.counter.SettingsStatsCollector
import org.jetbrains.research.deepbugs.services.ui.DeepBugsUI

import javax.swing.JComponent

abstract class DeepBugsInspectionConfigurable(protected val settings: DeepBugsInspectionConfig) : Configurable {
    private lateinit var deepBugsUI: DeepBugsUI

    private fun logSettings() {
        return SettingsStatsCollector.logNewSettings(settings, deepBugsUI)
    }

    abstract fun createUI(): DeepBugsUI

    override fun getHelpTopic(): String? = null

    override fun isModified() =
        (deepBugsUI.binOperatorThreshold != settings.curBinOperatorThreshold) ||
            (deepBugsUI.binOperandThreshold != settings.curBinOperandThreshold) ||
            (deepBugsUI.swappedArgsThreshold != settings.curSwappedArgsThreshold)


    override fun apply() {
        logSettings()
        settings.curBinOperatorThreshold = deepBugsUI.binOperatorThreshold
        settings.curBinOperandThreshold = deepBugsUI.binOperandThreshold
        settings.curSwappedArgsThreshold = deepBugsUI.swappedArgsThreshold

        //FIXME-review probably better use message-bus (like in Grazie)
        ProjectManager.getInstance().openProjects.forEach {
            DaemonCodeAnalyzer.getInstance(it).restart()
        }
    }

    override fun reset() {
        deepBugsUI.binOperatorThreshold = settings.curBinOperatorThreshold
        deepBugsUI.binOperandThreshold = settings.curBinOperandThreshold
        deepBugsUI.swappedArgsThreshold = settings.curSwappedArgsThreshold
    }

    override fun disposeUIResources() {}

    override fun createComponent(): JComponent? {
        deepBugsUI = createUI()
        deepBugsUI.binOperatorThreshold = settings.curBinOperatorThreshold
        deepBugsUI.binOperandThreshold = settings.curBinOperandThreshold
        deepBugsUI.swappedArgsThreshold = settings.curSwappedArgsThreshold
        return deepBugsUI.rootPanel
    }
}
