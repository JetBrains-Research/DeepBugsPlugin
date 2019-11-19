package org.jetbrains.research.deepbugs.services.model

import com.intellij.openapi.application.PathManager
import com.intellij.openapi.progress.*
import com.intellij.openapi.project.ProjectManager
import org.jetbrains.research.deepbugs.services.logger.collectors.counter.ErrorInfoCollector
import org.jetbrains.research.deepbugs.services.utils.DeepBugsServicesBundle
import org.jetbrains.research.deepbugs.services.utils.Mapping
import org.tensorflow.SavedModelBundle
import org.tensorflow.Session
import java.nio.file.Paths

abstract class ModelManager {
    protected abstract val pluginName: String
    private fun getModelPath() = Paths.get(PathManager.getPluginsPath(), pluginName, "models").toString()

    private var initFlag = false
    private lateinit var modelStorage: ModelStorage

    init {
        initModels()
    }

    fun getNodeTypes(): Mapping? {
        if (!initFlag) return null
        return modelStorage.nodeTypeMapping
    }
    fun getTypes(): Mapping? {
        if (!initFlag) return null
        return modelStorage.typeMapping
    }
    fun getTokens(): Mapping? {
        if (!initFlag) return null
        return modelStorage.tokenMapping
    }
    fun getOperators(): Mapping? {
        if (!initFlag) return null
        return modelStorage.operatorMapping
    }
    fun getBinOperatorModel(): Session? {
        if (!initFlag) return null
        return modelStorage.binOperatorModel
    }
    fun getBinOperandModel(): Session? {
        if (!initFlag) return null
        return modelStorage.binOperandModel
    }
    fun getSwappedArgsModel(): Session? {
        if (!initFlag) return null
        return modelStorage.swappedArgsModel
    }

    private fun initModels() {
        ProgressManager.getInstance().run(object : Task.Backgroundable(ProjectManager.getInstance().defaultProject,
            DeepBugsServicesBundle.message("initialize.task.title", pluginName), false) {
            override fun run(indicator: ProgressIndicator) {
                indicator.isIndeterminate = true
                modelStorage = ModelStorage(
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

            override fun onFinished() {
                initFlag = true
            }
        })
    }

    private fun loadMapping(mappingName: String, progress: ProgressIndicator): Mapping {
        val loadMappingPath = Paths.get(getModelPath(), mappingName)
        progress.text = DeepBugsServicesBundle.message("init.model.file", mappingName)
        return Mapping(loadMappingPath)
    }

    private fun loadModel(modelName: String, progress: ProgressIndicator): Session {
        val loadModelPath = Paths.get(getModelPath(), modelName)
        progress.text = DeepBugsServicesBundle.message("init.model.file", modelName)
        return SavedModelBundle.load(loadModelPath.toString(), "serve").session()
    }
}
