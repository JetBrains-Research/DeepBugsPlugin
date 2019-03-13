package org.jetbrains.research.groups.ml_methods.deepbugs.javascript.settings

import com.intellij.openapi.options.Configurable
import org.jetbrains.research.groups.ml_methods.deepbugs.javascript.utils.DeepBugsJSService
import org.jetbrains.research.groups.ml_methods.deepbugs.services.settings.DeepBugsInspectionConfig
import org.jetbrains.research.groups.ml_methods.deepbugs.services.settings.DeepBugsInspectionConfigurable
import org.jetbrains.research.groups.ml_methods.deepbugs.services.ui.DeepBugsUI
import javax.swing.JComponent

class JSDeepBugsInspectionConfigurable(settings: JSDeepBugsInspectionConfig) : DeepBugsInspectionConfigurable(settings), Configurable {
    companion object {
        const val JS_DEFAULT_BIN_OPERATOR_CONFIG: Float = 0.94f
        const val JS_DEFAULT_BIN_OPERAND_CONFIG: Float = 0.95f
        const val JS_DEFAULT_SWAPPED_ARGS_CONFIG: Float = 0.96f
    }

    override fun createUI() = JSDeepBugsUI()

    override fun getDisplayName() = DeepBugsJSService.JS_PLUGIN_NAME
}