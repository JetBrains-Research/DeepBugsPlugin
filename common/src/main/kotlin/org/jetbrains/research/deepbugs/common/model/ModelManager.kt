package org.jetbrains.research.deepbugs.common.model

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import org.jetbrains.research.deepbugs.common.DeepBugsPlugin
import org.jetbrains.research.deepbugs.common.ide.fus.collectors.counter.ErrorInfoCollector
import org.jetbrains.research.deepbugs.common.utils.Cbor
import org.jetbrains.research.deepbugs.common.utils.Mapping
import org.jetbrains.research.keras.runner.deserializer.ModelLoader
import org.jetbrains.research.keras.runner.nn.model.sequential.Perceptron
import java.io.File

object ModelManager : StartupActivity, DumbAware {
    private val modelFolder by lazy { File(DeepBugsPlugin.installationFolder, "models") }

    val storage: ModelStorage by lazy {
        ModelStorage(
            binOperandModel = loadModel("binOperandDetectionModel.h5")!!,
            binOperatorModel = loadModel("binOperatorDetectionModel.h5")!!,
            swappedArgsModel = loadModel("swappedArgsDetectionModel.h5")!!,
            nodeTypeMapping = loadMapping("nodeTypeToVector.cbor"),
            typeMapping = loadMapping("typeToVector.cbor"),
            operatorMapping = loadMapping("operatorToVector.cbor"),
            tokenMapping = loadMapping("tokenToVector.cbor")
        )
    }

    override fun runActivity(project: Project) {
        storage
    }

    private fun loadMapping(name: String): Mapping = Cbor.parse(File(modelFolder, name).readBytes(), Mapping.serializer())

    private fun loadModel(name: String): Perceptron? = try {
        ModelLoader.loadPerceptronModel(File(modelFolder, name))
    } catch (ex: Exception) {
        ErrorInfoCollector.logInitErrorReported()
        null
    }
}
