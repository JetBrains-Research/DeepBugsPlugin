package org.jetbrains.research.groups.ml_methods.deepbugs.utils

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.beust.klaxon.Parser
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.progress.ProgressIndicator
import org.tensorflow.SavedModelBundle
import org.tensorflow.Session
import java.nio.file.Files
import java.nio.file.Paths

object DeepBugsUtils {
    private val modelsRoot = Paths.get(PathManager.getPluginsPath(), "DeepBugsPlugin", "models").toString()

    private fun getPath(name: String) = Paths.get(modelsRoot, name)

    fun toJson(obj: Any): String {
        val sb = StringBuilder(Klaxon().toJsonString(obj))
        return (Parser().parse(sb) as JsonArray<*>).toJsonString(true)
    }

    fun loadMapping(name: String, progress: ProgressIndicator): Mapping? {
        if (Files.exists(getPath(name))) {
            progress.text = DeepBugsPluginBundle.message("init.model.file", name)
            return Mapping(Parser().parse(getPath(name).toString()) as JsonObject)
        }
        return null
    }

    fun loadModel(name: String, progress: ProgressIndicator): Session? {
        if (Files.exists(getPath(name))) {
            progress.text = DeepBugsPluginBundle.message("init.model.file", name)
            return SavedModelBundle.load(getPath(name).toString(), "serve").session()
        }
        return null
    }

}
