package org.jetbrains.research.deepbugs.python.ide.ui

import com.intellij.openapi.components.service
import org.jetbrains.research.deepbugs.common.DeepBugsConfig
import org.jetbrains.research.deepbugs.common.ide.ui.DeepBugsConfigurable
import org.jetbrains.research.deepbugs.python.PyDeepBugsConfig
import org.jetbrains.research.deepbugs.python.PyResourceBundle

class PyDeepBugsConfigurable : DeepBugsConfigurable(
    PyDeepBugsConfig.default,
    "PyPluginConfig",
    PyResourceBundle.message("deepbugs.python.display")
) {
    override fun getSettings(): DeepBugsConfig = service<PyDeepBugsConfig>()
}
