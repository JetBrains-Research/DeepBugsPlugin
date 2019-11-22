package org.jetbrains.research.deepbugs.javascript.ide.ui

import com.intellij.ide.plugins.PluginManager
import com.intellij.openapi.options.Configurable
import org.jetbrains.research.deepbugs.common.ide.fus.collectors.counter.SettingsStatsCollector
import org.jetbrains.research.deepbugs.common.ide.ui.DeepBugsConfigurable
import org.jetbrains.research.deepbugs.common.ide.ui.DeepBugsUI
import org.jetbrains.research.deepbugs.javascript.JSDeepBugsConfig

class JSDeepBugsConfigurable(settings: JSDeepBugsConfig) : DeepBugsConfigurable(settings), Configurable {
    override fun createUI(): DeepBugsUI {
        SettingsStatsCollector.logSettingsInvoked(settings.configId)
        return JSDeepBugsUI()
    }

    override fun getDisplayName(): String {
        //TODO move this functionality to separate class
        return PluginManager.getPluginByClassName(JSDeepBugsConfigurable::class.java.name)!!.let {
            PluginManager.getPlugin(it)!!.name
        }
    }
}
