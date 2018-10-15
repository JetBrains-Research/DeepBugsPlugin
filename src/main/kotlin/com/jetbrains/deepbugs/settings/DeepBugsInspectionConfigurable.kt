package com.jetbrains.deepbugs.settings

import com.intellij.openapi.options.Configurable
import com.jetbrains.deepbugs.gui.DeepBugsGUI
import javax.swing.JComponent

class DeepBugsInspectionConfigurable(private val settings : DeepBugsInspectionConfig) : Configurable {

    private var deepBugsGUI : DeepBugsGUI? = null

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
        deepBugsGUI = DeepBugsGUI()
        deepBugsGUI!!.threshold = settings.threshold
        return deepBugsGUI!!.rootPanel
    }
}