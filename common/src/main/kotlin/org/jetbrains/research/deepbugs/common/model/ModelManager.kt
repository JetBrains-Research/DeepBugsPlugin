package org.jetbrains.research.deepbugs.common.model

import com.intellij.openapi.application.PathManager
import com.intellij.openapi.progress.*
import com.intellij.openapi.project.ProjectManager
import org.jetbrains.research.deepbugs.common.logger.collectors.counter.ErrorInfoCollector
import org.jetbrains.research.deepbugs.common.CommonResourceBundle
import org.jetbrains.research.deepbugs.common.utils.Mapping
import org.tensorflow.SavedModelBundle
import org.tensorflow.Session
import java.nio.file.Paths

abstract class ModelManager {
    protected abstract val pluginName: String
    private fun getModelPath() = Paths.get(PathManager.getPluginsPath(), pluginName, "models").toString()

    var storage: ModelStorage? = null
        private set

    init {
        initModels()
    }

    private fun initModels() {
        ProgressManager.getInstance().run(object : Task.Backgroundable(ProjectManager.getInstance().defaultProject,
            CommonResourceBundle.message("initialize.task.title", pluginName), false) {
            override fun run(indicator: ProgressIndicator) {
                indicator.isIndeterminate = true
                storage = ModelStorage(
                    binOperandModel = loadModel("binOperandDetectionModel", indicator),
                    binOperatorModel = loadModel("binOperatorDetectionModel", indicator),
                    swappedArgsModel = loadModel("swappedArgsDetectionModel", indicator),
                    nodeTypeMapping = loadMapping("nodeTypeToVector.json", indicator),
                    typeMapping = loadMapping("typeToVector.json", indicator),
                    operatorMapping = loadMapping("operatorToVector.json", indicator),
                    tokenMapping = loadMapping("tokenToVector.json", indicator)
                )
            }

            override fun onThrowable(error: Throwable) {
                if (error !is UnsatisfiedLinkError) return
                ErrorInfoCollector.logInitErrorReported()
            }
        })
    }

    private fun loadMapping(mappingName: String, progress: ProgressIndicator): Mapping {
        val loadMappingPath = Paths.get(getModelPath(), mappingName)
        progress.text = CommonResourceBundle.message("init.model.file", mappingName)
        return Mapping(loadMappingPath)
    }

    private fun loadModel(modelName: String, progress: ProgressIndicator): Session {
        val loadModelPath = Paths.get(getModelPath(), modelName)
        progress.text = CommonResourceBundle.message("init.model.file", modelName)
        return SavedModelBundle.load(loadModelPath.toString(), "serve").session()
    }
}
