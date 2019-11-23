package org.jetbrains.research.deepbugs.common.model

import com.intellij.openapi.application.PathManager
import com.intellij.openapi.progress.*
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.startup.StartupActivity
import org.jetbrains.research.deepbugs.common.ide.fus.collectors.counter.ErrorInfoCollector
import org.jetbrains.research.deepbugs.common.CommonResourceBundle
import org.jetbrains.research.deepbugs.common.DeepBugsPlugin
import org.jetbrains.research.deepbugs.common.utils.Mapping
import org.tensorflow.SavedModelBundle
import org.tensorflow.Session
import java.nio.file.Paths

object ModelManager: StartupActivity, DumbAware {
    private val modelPath by lazy { Paths.get(PathManager.getPluginsPath(), DeepBugsPlugin.pluginName, "models").toString() }

    var storage: ModelStorage? = null
        private set

    override fun runActivity(project: Project) {
        initModels()
    }

    private fun initModels() {
        ProgressManager.getInstance().run(object : Task.Backgroundable(ProjectManager.getInstance().defaultProject,
            CommonResourceBundle.message("initialize.task.title", DeepBugsPlugin.pluginName), false) {
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
        val loadMappingPath = Paths.get(modelPath, mappingName)
        progress.text = CommonResourceBundle.message("init.model.file", mappingName)
        return Mapping(loadMappingPath)
    }

    private fun loadModel(modelName: String, progress: ProgressIndicator): Session {
        val loadModelPath = Paths.get(modelPath, modelName)
        progress.text = CommonResourceBundle.message("init.model.file", modelName)
        return SavedModelBundle.load(loadModelPath.toString(), "serve").session()
    }
}
