package org.jetbrains.research.groups.ml_methods.deepbugs.python.utils

import org.jetbrains.research.groups.ml_methods.deepbugs.services.model_storage.ModelStorage

val models by lazy { ModelStorage(DeepBugsPythonService.PY_PLUGIN_NAME) }

object DeepBugsPythonService {
    const val PY_PLUGIN_NAME = "DeepBugsPython"
    const val PY_RECORDER_VERSION = "1"

    //TODO: implement log reporter
}