package org.jetbrains.research.deepbugs.javascript.ide.ui

import com.intellij.openapi.components.ServiceManager
import org.jetbrains.research.deepbugs.common.DeepBugsConfig
import org.jetbrains.research.deepbugs.common.ide.ui.DeepBugsConfigurable
import org.jetbrains.research.deepbugs.common.ide.ui.DeepBugsSettingsPanel
import org.jetbrains.research.deepbugs.javascript.JSDeepBugsConfig
import org.jetbrains.research.deepbugs.javascript.JSResourceBundle

class JSDeepBugsConfigurable : DeepBugsConfigurable(JSDeepBugsConfig.default, "deepbugs.js.configurable", JSResourceBundle.message("deepbugs.javascript.display")) {
    override fun createUi(): DeepBugsSettingsPanel = DeepBugsSettingsPanel(settings, default)

    override fun getSettings(): DeepBugsConfig = ServiceManager.getService(JSDeepBugsConfig::class.java)
}
