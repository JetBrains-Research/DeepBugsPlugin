package org.jetbrains.research.deepbugs.common.ide.ui

import com.intellij.openapi.options.ConfigurableBase
import org.jetbrains.research.deepbugs.common.DeepBugsConfig

abstract class DeepBugsConfigurable(
    private val default: DeepBugsConfig.State,
    id: String,
    display: String
) : ConfigurableBase<DeepBugsSettingsPanel, DeepBugsConfig>(id, display, null) {
    override fun createUi(): DeepBugsSettingsPanel = DeepBugsSettingsPanel(settings, default)
}
