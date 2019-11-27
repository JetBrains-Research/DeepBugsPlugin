package org.jetbrains.research.deepbugs.common.ide.ui

import com.intellij.openapi.options.ConfigurableBase
import org.jetbrains.research.deepbugs.common.DeepBugsConfig

abstract class DeepBugsConfigurable(
    protected val default: DeepBugsConfig.State,
    id: String,
    display: String
) : ConfigurableBase<DeepBugsSettingsPanel, DeepBugsConfig>(id, display, null) {
    private lateinit var ui: DeepBugsSettingsPanel

    override fun createUi(): DeepBugsSettingsPanel = DeepBugsSettingsPanel(settings, default).also { ui = it }
}
