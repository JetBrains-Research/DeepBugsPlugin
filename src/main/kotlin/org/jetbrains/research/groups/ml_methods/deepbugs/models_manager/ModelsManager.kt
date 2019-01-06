package org.jetbrains.research.groups.ml_methods.deepbugs.models_manager

import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.ProjectManager
import org.jetbrains.research.groups.ml_methods.deepbugs.downloader.DownloadClient
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.DeepBugsPluginBundle
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.DeepBugsUtils
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.Mapping
import org.tensorflow.Session

object ModelsManager {

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