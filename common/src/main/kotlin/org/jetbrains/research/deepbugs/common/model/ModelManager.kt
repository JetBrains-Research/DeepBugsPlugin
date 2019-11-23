package org.jetbrains.research.deepbugs.common.model

import com.intellij.openapi.application.PathManager
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import org.jetbrains.research.deepbugs.common.DeepBugsPlugin
import org.jetbrains.research.deepbugs.common.utils.*
import org.tensorflow.SavedModelBundle
import org.tensorflow.Session
import java.io.File
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
            nodeTypeMapping = loadMapping("nodeTypeToVector.cbor"),
            typeMapping = loadMapping("typeToVector.cbor"),
            operatorMapping = loadMapping("operatorToVector.cbor"),
            tokenMapping = loadMapping("tokenToVector.cbor")
        )
    }

    private fun loadMapping(mapping: String): Mapping {
        val cborFile = File(modelPath, mapping)
        return Cbor.parse(cborFile.readBytes(), Mapping.serializer())
    }

    private fun loadModel(modelName: String): Session = SavedModelBundle.load(Paths.get(modelPath, modelName).toString(), "serve").session()
}
