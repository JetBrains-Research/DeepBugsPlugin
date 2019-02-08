package org.jetbrains.research.groups.ml_methods.deepbugs.services.models_manager

import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.ProjectManager
import org.jetbrains.research.groups.ml_methods.deepbugs.services.downloader.DownloadClient
import org.jetbrains.research.groups.ml_methods.deepbugs.services.utils.DeepBugsPluginServicesBundle
import org.jetbrains.research.groups.ml_methods.deepbugs.services.utils.Mapping
import org.jetbrains.research.groups.ml_methods.deepbugs.services.utils.ModelUtils
import org.jetbrains.research.groups.ml_methods.deepbugs.services.utils.PlatformUtils
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

    fun initModels() {
        ProgressManager.getInstance().run(object : Task.Backgroundable(ProjectManager.getInstance().defaultProject,
                DeepBugsPluginServicesBundle.message("initialize.task.title", PlatformUtils.getPluginName()), false) {
            override fun run(indicator: ProgressIndicator) {
                indicator.isIndeterminate = true
                nodeTypeMapping = ModelUtils.loadMapping("nodeTypeToVector.json", indicator)
                typeMapping = ModelUtils.loadMapping("typeToVector.json", indicator)
                operatorMapping = ModelUtils.loadMapping("operatorToVector.json", indicator)
                tokenMapping = ModelUtils.loadMapping("tokenToVector.json", indicator)
                binOperandModel = ModelUtils.loadModel("binOperandDetectionModel", indicator)
                binOperatorModel = ModelUtils.loadModel("binOperatorDetectionModel", indicator)
                swappedArgsModel = ModelUtils.loadModel("swappedArgsDetectionModel", indicator)
            }
        })
    }
}