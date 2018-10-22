package org.jetbrains.research.groups.ml_methods.deepbugs.settings

import com.intellij.openapi.options.Configurable
import org.jetbrains.research.groups.ml_methods.deepbugs.ui.DeepBugsUI
import javax.swing.JComponent

class DeepBugsInspectionConfigurable(private val settings : DeepBugsInspectionConfig) : Configurable {

    companion object {
        const val defaultConfig = 0.89f
    }

    private var deepBugsGUI : DeepBugsUI? = null

    override fun getHelpTopic(): String? = null

    override fun getDisplayName() = "DeepBugs"

    override fun isModified() = deepBugsGUI!!.threshold != settings.threshold

    override fun apply() {
        settings.threshold = deepBugsGUI!!.threshold
    }

    override fun reset() {
        deepBugsGUI!!.threshold = settings.threshold
    }

    override fun disposeUIResources() {
        deepBugsGUI = null
    }

    override fun createComponent() : JComponent? {
        deepBugsGUI = DeepBugsUI()
        deepBugsGUI!!.threshold = settings.threshold
        return deepBugsGUI!!.rootPanel
    }
}