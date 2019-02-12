package org.jetbrains.research.groups.ml_methods.deepbugs.javascript.settings

import com.intellij.openapi.options.Configurable
import org.jetbrains.research.groups.ml_methods.deepbugs.javascript.utils.DeepBugsJSService
import org.jetbrains.research.groups.ml_methods_deepbugs.logger.logging.events.ThresholdConfigured
import org.jetbrains.research.groups.ml_methods_deepbugs.logger.logging.events.ThresholdFeatures
import javax.swing.JComponent

class JSDeepBugsInspectionConfigurable(private val settings: JSDeepBugsInspectionConfig) : Configurable {
    companion object {
        const val DEFAULT_BIN_OPERATOR_CONFIG: Float = 0.94f
        const val DEFAULT_BIN_OPERAND_CONFIG: Float = 0.95f
        const val DEFAULT_SWAPPED_ARGS_CONFIG: Float = 0.96f
    }

    private var deepBugsUI: JSDeepBugsUI? = null

    override fun getHelpTopic(): String? = null

    override fun getDisplayName() = DeepBugsJSService.JS_PLUGIN_NAME

    override fun isModified() =
            (deepBugsUI!!.binOperatorThreshold != settings.curBinOperatorThreshold) ||
                    (deepBugsUI!!.binOperandThreshold != settings.curBinOperandThreshold) ||
                    (deepBugsUI!!.swappedArgsThreshold != settings.curSwappedArgsThreshold)


    override fun apply() {
        val configFeatures = ThresholdConfigured(
                ThresholdFeatures(settings.curBinOperatorThreshold, deepBugsUI!!.binOperatorThreshold),
                ThresholdFeatures(settings.curBinOperandThreshold, deepBugsUI!!.binOperandThreshold),
                ThresholdFeatures(settings.curSwappedArgsThreshold, deepBugsUI!!.swappedArgsThreshold)
        )
        settings.curBinOperatorThreshold = deepBugsUI!!.binOperatorThreshold
        settings.curBinOperandThreshold = deepBugsUI!!.binOperandThreshold
        settings.curSwappedArgsThreshold = deepBugsUI!!.swappedArgsThreshold
        DeepBugsJSService.sendConfigLog(configFeatures)
    }

    override fun reset() {
        deepBugsUI!!.binOperatorThreshold = settings.curBinOperatorThreshold
        deepBugsUI!!.binOperandThreshold = settings.curBinOperandThreshold
        deepBugsUI!!.swappedArgsThreshold = settings.curSwappedArgsThreshold
    }

    override fun disposeUIResources() {
        deepBugsUI = null
    }

    override fun createComponent(): JComponent? {
        deepBugsUI = JSDeepBugsUI()
        deepBugsUI!!.binOperatorThreshold = settings.curBinOperatorThreshold
        deepBugsUI!!.binOperandThreshold = settings.curBinOperandThreshold
        deepBugsUI!!.swappedArgsThreshold = settings.curSwappedArgsThreshold
        return deepBugsUI!!.rootPanel
    }
}