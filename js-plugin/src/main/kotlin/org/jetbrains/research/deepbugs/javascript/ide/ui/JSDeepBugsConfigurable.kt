package org.jetbrains.research.deepbugs.javascript.ide.ui

import com.intellij.openapi.components.service
import org.jetbrains.research.deepbugs.common.DeepBugsConfig
import org.jetbrains.research.deepbugs.common.ide.ui.DeepBugsConfigurable
import org.jetbrains.research.deepbugs.javascript.JSDeepBugsConfig
import org.jetbrains.research.deepbugs.javascript.JSResourceBundle

class JSDeepBugsConfigurable : DeepBugsConfigurable(
    JSDeepBugsConfig.default,
    "JSPluginConfig",
    JSResourceBundle.message("deepbugs.javascript.display")
) {
    override fun getSettings(): DeepBugsConfig = service<JSDeepBugsConfig>()
}
