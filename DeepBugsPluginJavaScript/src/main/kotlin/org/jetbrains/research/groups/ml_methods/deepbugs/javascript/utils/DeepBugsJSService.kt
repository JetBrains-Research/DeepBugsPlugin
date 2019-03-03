package org.jetbrains.research.groups.ml_methods.deepbugs.javascript.utils

import org.jetbrains.research.groups.ml_methods.deepbugs.services.models_manager.ModelsStorage


val models by lazy { ModelsStorage(DeepBugsJSService.JS_PLUGIN_NAME) }

object DeepBugsJSService {
    const val JS_PLUGIN_NAME = "DeepBugsJavaScript"
    const val JS_RECORDER_VERSION = "1"

    //TODO: implement log reporter
    //val logService = DeepBugsLogService(JS_PLUGIN_NAME, JS_RECORDER_VERSION)
}