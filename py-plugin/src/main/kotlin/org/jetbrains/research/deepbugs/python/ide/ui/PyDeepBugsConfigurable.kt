package org.jetbrains.research.deepbugs.python.ide.ui

import com.intellij.ide.plugins.PluginManager
import com.intellij.openapi.options.Configurable
import org.jetbrains.research.deepbugs.common.ide.fus.collectors.counter.SettingsStatsCollector
import org.jetbrains.research.deepbugs.common.ide.ui.DeepBugsConfigurable
import org.jetbrains.research.deepbugs.common.ide.ui.DeepBugsUI
import org.jetbrains.research.deepbugs.python.PyDeepBugsConfig

class PyDeepBugsConfigurable(settings: PyDeepBugsConfig) : DeepBugsConfigurable(settings), Configurable {
    override fun createUI(): DeepBugsUI {
        SettingsStatsCollector.logSettingsInvoked(settings.configId)
        return PyDeepBugsUI()
    }

    override fun getDisplayName(): String {
        //TODO move this functionality to separate class
        return PluginManager.getPluginByClassName(PyDeepBugsConfigurable::class.java.name)!!.let {
            PluginManager.getPlugin(it)!!.name
        }
    }
}
