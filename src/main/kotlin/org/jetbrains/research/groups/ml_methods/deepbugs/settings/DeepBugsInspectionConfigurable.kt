package org.jetbrains.research.groups.ml_methods.deepbugs.settings

import com.intellij.openapi.options.Configurable
import org.jetbrains.research.groups.ml_methods.deepbugs.ui.DeepBugsUI
import javax.swing.JComponent

class DeepBugsInspectionConfigurable(private val settings : DeepBugsInspectionConfig) : Configurable {

    companion object {
        const val defaultBinOperatorConfig = 0.9f
        const val defaultBinOperandConfig = 0.92f
    }

    private var deepBugsUI : DeepBugsUI? = null

    override fun getHelpTopic(): String? = null

    override fun getDisplayName() = "DeepBugs"

    override fun isModified() =
            (deepBugsUI!!.binOperatorThreshold != settings.curBinOperatorThreshold) ||
                    (deepBugsUI!!.binOperandThreshold != settings.curBinOperandThreshold)

    override fun apply() {
        settings.curBinOperatorThreshold = deepBugsUI!!.binOperatorThreshold
        settings.curBinOperandThreshold = deepBugsUI!!.binOperandThreshold
    }

    override fun reset() {
        deepBugsUI!!.binOperatorThreshold = settings.curBinOperatorThreshold
        deepBugsUI!!.binOperandThreshold = settings.curBinOperandThreshold
    }

    override fun disposeUIResources() {
        deepBugsUI = null
    }

    override fun createComponent() : JComponent? {
        deepBugsUI = DeepBugsUI()
        deepBugsUI!!.binOperatorThreshold = settings.curBinOperatorThreshold
        deepBugsUI!!.binOperandThreshold = settings.curBinOperandThreshold
        return deepBugsUI!!.rootPanel
    }
}