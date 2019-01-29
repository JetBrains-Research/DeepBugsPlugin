package org.jetbrains.research.groups.ml_methods.deepbugs.services.utils

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.progress.ProgressIndicator
import org.tensorflow.SavedModelBundle
import org.tensorflow.Session
import java.nio.file.Files
import java.nio.file.Paths

object ModelUtils {
    private fun getModelPath(pluginName: String, modelName: String) =
            Paths.get(PathManager.getPluginsPath(), pluginName, "models", modelName)

    fun loadMapping(pluginName: String, modelName: String, progress: ProgressIndicator): Mapping? {
        val modelPath = getModelPath(pluginName, modelName)
        if (Files.exists(modelPath)) {
            progress.text = DeepBugsPluginServicesBundle.message("init.model.file", modelName)
            return Mapping(Parser().parse(modelPath.toString()) as JsonObject)
        }
        return null
    }

    fun loadModel(pluginName: String, modelName: String, progress: ProgressIndicator): Session? {
        val modelPath = getModelPath(pluginName, modelName)
        if (Files.exists(modelPath)) {
            progress.text = DeepBugsPluginServicesBundle.message("init.model.file", modelName)
            return SavedModelBundle.load(modelPath.toString(), "serve").session()
        }
        return null
    }
}