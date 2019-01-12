package org.jetbrains.research.groups.ml_methods.deepbugs.models_manager

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.ProjectManager
import org.jetbrains.research.groups.ml_methods.deepbugs.downloader.DownloadClient
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.DeepBugsPluginBundle
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.Mapping
import org.tensorflow.SavedModelBundle
import org.tensorflow.Session
import java.nio.file.Files
import java.nio.file.Paths

object ModelsManager {

    private val modelsRoot = Paths.get(PathManager.getPluginsPath(), "DeepBugsPlugin", "models").toString()
    var nodeTypeMapping: Mapping? = null
        private set
    var tokenMapping: Mapping? = null
        private set
    var operatorMapping: Mapping? = null
        private set
    var typeMapping: Mapping? = null
        private set
    var binOperatorModel: Session? = null
        private set
    var binOperandModel: Session? = null
        private set
    var swappedArgsModel: Session? = null
        private set

    init {
        DownloadClient.checkRepos()
    }

    private fun getPath(name: String) = Paths.get(modelsRoot, name)

    private fun loadMapping(name: String, progress: ProgressIndicator): Mapping? {
        if (Files.exists(getPath(name))) {
            progress.text = DeepBugsPluginBundle.message("init.model.file", name)
            return Mapping(Parser().parse(getPath(name).toString()) as JsonObject)
        }
        return null
    }

    private fun loadModel(name: String, progress: ProgressIndicator): Session? {
        if (Files.exists(getPath(name))) {
            progress.text = DeepBugsPluginBundle.message("init.model.file", name)
            return SavedModelBundle.load(getPath(name).toString(), "serve").session()
        }
        return null
    }

    private fun initMappings(progress: ProgressIndicator) {
        nodeTypeMapping = loadMapping("nodeTypeToVector.json", progress)
        typeMapping = loadMapping("typeToVector.json", progress)
        operatorMapping = loadMapping("operatorToVector.json", progress)
        tokenMapping = loadMapping("tokenToVector.json", progress)
    }

    private fun initBinOperandModel(progress: ProgressIndicator) {
        binOperandModel = loadModel("binOperandDetectionModel", progress)
    }

    private fun initBinOperatorModel(progress: ProgressIndicator) {
        binOperatorModel = loadModel("binOperatorDetectionModel", progress)
    }

    private fun initSwappedArgsModel(progress: ProgressIndicator) {
        swappedArgsModel = loadModel("swappedArgsDetectionModel", progress)
    }

    fun initModels() {
        ProgressManager.getInstance().run(object : Task.Backgroundable(ProjectManager.getInstance().defaultProject,
                DeepBugsPluginBundle.message("initialize.task.title"), false) {
            override fun run(indicator: ProgressIndicator) {
                indicator.isIndeterminate = true
                initMappings(indicator)
                initBinOperandModel(indicator)
                initBinOperatorModel(indicator)
                initSwappedArgsModel(indicator)
            }
        })
    }
}