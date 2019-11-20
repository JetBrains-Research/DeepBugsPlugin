package org.jetbrains.research.deepbugs.javascript.settings

import com.intellij.ide.plugins.PluginManager
import com.intellij.openapi.options.Configurable

import org.jetbrains.research.deepbugs.common.logger.collectors.counter.SettingsStatsCollector
import org.jetbrains.research.deepbugs.common.settings.DeepBugsInspectionConfigurable
import org.jetbrains.research.deepbugs.common.ui.DeepBugsUI

class JSDeepBugsInspectionConfigurable(settings: JSDeepBugsInspectionConfig)
    : DeepBugsInspectionConfigurable(settings), Configurable {
    override fun createUI(): DeepBugsUI {
        SettingsStatsCollector.logSettingsInvoked(settings.configId)
        return JSDeepBugsUI()
    }

    override fun getDisplayName(): String {
        return PluginManager.getPluginByClassName(JSDeepBugsInspectionConfigurable::class.java.name)!!.idString
    }

    companion object {
        const val JS_DEFAULT_BIN_OPERATOR_CONFIG: Float = 0.94f
        const val JS_DEFAULT_BIN_OPERAND_CONFIG: Float = 0.95f
        const val JS_DEFAULT_SWAPPED_ARGS_CONFIG: Float = 0.96f
    }
}
