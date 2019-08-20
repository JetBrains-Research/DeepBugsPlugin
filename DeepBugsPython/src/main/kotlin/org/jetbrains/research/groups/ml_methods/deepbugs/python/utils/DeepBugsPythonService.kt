package org.jetbrains.research.groups.ml_methods.deepbugs.python.utils

import org.jetbrains.research.groups.ml_methods.deepbugs.services.model_storage.ModelStorage

object DeepBugsPythonService {
    val models by lazy { ModelStorage(PY_PLUGIN_NAME) }

    const val PY_PLUGIN_NAME = "DeepBugsPython"
}