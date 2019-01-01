package org.jetbrains.research.groups.ml_methods.deepbugs.models_manager

import com.intellij.openapi.application.PathManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.ProjectManager
import com.intellij.util.io.exists
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.jetbrains.research.groups.ml_methods.deepbugs.DeepBugsProvider
import org.jetbrains.research.groups.ml_methods.deepbugs.downloader.Config
import org.jetbrains.research.groups.ml_methods.deepbugs.downloader.DownloadClient
import org.jetbrains.research.groups.ml_methods.deepbugs.downloader.RepositoryRecord
import org.jetbrains.research.groups.ml_methods.deepbugs.downloader.utils.JsonUtils
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.DeepBugsJsonUtils
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.DeepBugsPluginBundle
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.Mapping
import java.nio.file.Paths
import kotlin.reflect.KClass

object ModelsHolder {
    init {
        if (!modelFilesExists())
            DownloadClient.showDownloadNotification()
        else
            initModels()
    }

    private val root = Paths.get(PathManager.getPluginsPath(), "DeepBugsPlugin").toString()
    var nodeTypeMapping: Mapping? = null
    var tokenMapping: Mapping? = null
    var operatorMapping: Mapping? = null
    var typeMapping: Mapping? = null
    var binOperatorModel: MultiLayerNetwork? = null
    var binOperandModel: MultiLayerNetwork? = null
    var swappedArgsModel: MultiLayerNetwork? = null

    private fun modelFilesExists(): Boolean {
        val localRepoPath = Paths.get(PathManager.getPluginsPath(), "DeepBugsPlugin", "repository.json")
        if (!localRepoPath.exists())
            return false
        val remoteRepoStr = DeepBugsProvider::class.java.classLoader.getResource("models.json").readText()
        val remoteRepo = JsonUtils.readValue(remoteRepoStr, Config::class).classpath
        val localRepo = JsonUtils.readCollectionValue(localRepoPath.toFile().readText(),
                MutableList::class as KClass<MutableList<RepositoryRecord>>, RepositoryRecord::class)
        remoteRepo.forEach { record ->
            localRepo.firstOrNull { it.name == record.name } ?: return false
        }
        return true
    }

    private fun checkMappings(progress: ProgressIndicator) {
        if (Paths.get(root, "models", "nodeTypeToVector.json").exists() && nodeTypeMapping == null) {
            progress.text = DeepBugsPluginBundle.message("init.node.types")
            nodeTypeMapping = DeepBugsJsonUtils.loadMapping(Paths.get(root, "models", "nodeTypeToVector.json").toString())
        }
        if (Paths.get(root, "models", "typeToVector.json").exists() && typeMapping == null) {
            progress.text = DeepBugsPluginBundle.message("init.types")
            typeMapping = DeepBugsJsonUtils.loadMapping(Paths.get(root, "models", "typeToVector.json").toString())
        }
        if (Paths.get(root, "models", "operatorToVector.json").exists() && operatorMapping == null) {
            operatorMapping = DeepBugsJsonUtils.loadMapping(Paths.get(root, "models", "operatorToVector.json").toString())
            progress.text = DeepBugsPluginBundle.message("init.operators")
        }
        if (Paths.get(root, "models", "tokenToVector.json").exists() && tokenMapping == null) {
            progress.text = DeepBugsPluginBundle.message("init.tokens")
            tokenMapping = DeepBugsJsonUtils.loadMapping(Paths.get(root, "models", "tokenToVector.json").toString())
        }
    }

    private fun checkBinOperandModel(progress: ProgressIndicator) {
        if (Paths.get(root, "models", "binOperandDetectionModel.h5").exists() && binOperandModel == null) {
            progress.text = DeepBugsPluginBundle.message("init.bin.operand.model")
            binOperandModel = KerasModelImport.importKerasSequentialModelAndWeights(Paths.get(root, "models", "binOperandDetectionModel.h5").toString())
        }
    }

    private fun checkBinOperatorModel(progress: ProgressIndicator) {
        if (Paths.get(root, "models", "binOperatorDetectionModel.h5").exists() && binOperatorModel == null) {
            progress.text = DeepBugsPluginBundle.message("init.bin.operator.model")
            binOperatorModel = KerasModelImport.importKerasSequentialModelAndWeights(Paths.get(root, "models", "binOperatorDetectionModel.h5").toString())
        }
    }

    private fun checkSwappedArgsModel(progress: ProgressIndicator) {
        if (Paths.get(root, "models", "swappedArgsDetectionModel.h5").exists() && swappedArgsModel == null) {
            progress.text = DeepBugsPluginBundle.message("init.swapped.args.model")
            swappedArgsModel = KerasModelImport.importKerasSequentialModelAndWeights(Paths.get(root, "models", "swappedArgsDetectionModel.h5").toString())
        }
    }

    fun initModels() {
        ProgressManager.getInstance().run(object : Task.Backgroundable(ProjectManager.getInstance().defaultProject,
                DeepBugsPluginBundle.message("initialize.task.title"), false) {
            override fun run(indicator: ProgressIndicator) {
                indicator.isIndeterminate = true
                checkMappings(indicator)
                checkBinOperandModel(indicator)
                checkBinOperatorModel(indicator)
                checkSwappedArgsModel(indicator)
            }
        })
    }
}