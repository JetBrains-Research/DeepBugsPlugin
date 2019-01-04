package org.jetbrains.research.groups.ml_methods.deepbugs.models_manager

import com.intellij.openapi.application.PathManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.ProjectManager
import org.jetbrains.research.groups.ml_methods.deepbugs.downloader.Config
import org.jetbrains.research.groups.ml_methods.deepbugs.downloader.DownloadClient
import org.jetbrains.research.groups.ml_methods.deepbugs.downloader.RepositoryRecord
import org.jetbrains.research.groups.ml_methods.deepbugs.downloader.utils.JsonUtils
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.DeepBugsPluginBundle
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.DeepBugsUtils
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.Mapping
import org.tensorflow.Session
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.reflect.KClass

object ModelsManager {

    private val root = Paths.get(PathManager.getPluginsPath(), "DeepBugsPlugin").toString()
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
        when (modelFilesExists()) {
            true -> initModels()
            false -> DownloadClient.showDownloadNotification()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun modelFilesExists(): Boolean {
        val localRepoPath = Paths.get(root, "repository.json")
        if (!Files.exists(localRepoPath))
            return false
        val localRepo = JsonUtils.readCollectionValue(localRepoPath.toFile().readText(),
                MutableList::class as KClass<MutableList<RepositoryRecord>>, RepositoryRecord::class)
        val remoteRepoStr = ModelsManager::class.java.classLoader.getResource("models.json").readText()
        val remoteRepo = JsonUtils.readValue(remoteRepoStr, Config::class).classpath
        remoteRepo.forEach { record ->
            localRepo.firstOrNull { it.name == record.name } ?: return false
        }
        return true
    }

    private fun initMappings(progress: ProgressIndicator) {
        nodeTypeMapping = DeepBugsUtils.loadMapping("nodeTypeToVector.json", progress)
        typeMapping = DeepBugsUtils.loadMapping("typeToVector.json", progress)
        operatorMapping = DeepBugsUtils.loadMapping("operatorToVector.json", progress)
        tokenMapping = DeepBugsUtils.loadMapping("tokenToVector.json", progress)
    }

    private fun initBinOperandModel(progress: ProgressIndicator) {
        binOperandModel = DeepBugsUtils.loadModel("binOperandDetectionModel", progress)
    }

    private fun initBinOperatorModel(progress: ProgressIndicator) {
        binOperatorModel = DeepBugsUtils.loadModel("binOperatorDetectionModel", progress)
    }

    private fun initSwappedArgsModel(progress: ProgressIndicator) {
        swappedArgsModel = DeepBugsUtils.loadModel("swappedArgsDetectionModel", progress)
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