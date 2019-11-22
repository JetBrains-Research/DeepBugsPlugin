package org.jetbrains.research.deepbugs.python.settings

import com.intellij.ide.plugins.PluginManager
import com.intellij.openapi.options.Configurable
import org.jetbrains.research.deepbugs.common.logger.collectors.counter.SettingsStatsCollector
import org.jetbrains.research.deepbugs.common.settings.DeepBugsInspectionConfigurable
import org.jetbrains.research.deepbugs.common.ui.DeepBugsUI

class PyDeepBugsInspectionConfigurable(settings: PyDeepBugsInspectionConfig) : DeepBugsInspectionConfigurable(settings), Configurable {
    override fun createUI(): DeepBugsUI {
        SettingsStatsCollector.logSettingsInvoked(settings.configId)
        return PyDeepBugsUI()
    }

    override fun getDisplayName(): String {
        //TODO move this functionality to separate class
        return PluginManager.getPluginByClassName(PyDeepBugsInspectionConfigurable::class.java.name)!!.let {
            PluginManager.getPlugin(it)!!.name
        }
    }

    companion object {
        const val PY_DEFAULT_BIN_OPERATOR_CONFIG: Float = 0.94f
        const val PY_DEFAULT_BIN_OPERAND_CONFIG: Float = 0.95f
        const val PY_DEFAULT_SWAPPED_ARGS_CONFIG: Float = 0.96f
    }
}
