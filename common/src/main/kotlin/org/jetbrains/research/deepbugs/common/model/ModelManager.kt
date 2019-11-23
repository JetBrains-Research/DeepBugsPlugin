package org.jetbrains.research.deepbugs.common.model

import com.intellij.openapi.application.PathManager
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import org.jetbrains.research.deepbugs.common.DeepBugsPlugin
import org.jetbrains.research.deepbugs.common.utils.Mapping
import org.tensorflow.SavedModelBundle
import org.tensorflow.Session
import java.nio.file.Paths

object ModelManager : StartupActivity, DumbAware {
    private val modelPath by lazy { Paths.get(PathManager.getPluginsPath(), DeepBugsPlugin.pluginName, "models").toString() }

    var storage: ModelStorage? = null
        private set

    override fun runActivity(project: Project) {
        storage = ModelStorage(
            binOperandModel = loadModel("binOperandDetectionModel"),
            binOperatorModel = loadModel("binOperatorDetectionModel"),
            swappedArgsModel = loadModel("swappedArgsDetectionModel"),
            nodeTypeMapping = loadMapping("nodeTypeToVector.json"),
            typeMapping = loadMapping("typeToVector.json"),
            operatorMapping = loadMapping("operatorToVector.json"),
            tokenMapping = loadMapping("tokenToVector.json")
        )
    }

    private fun loadMapping(mappingName: String) = Mapping(Paths.get(modelPath, mappingName))

    private fun loadModel(modelName: String): Session = SavedModelBundle.load(Paths.get(modelPath, modelName).toString(), "serve").session()
}
