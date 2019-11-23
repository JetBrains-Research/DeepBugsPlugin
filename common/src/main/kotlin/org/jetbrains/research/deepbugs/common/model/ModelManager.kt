package org.jetbrains.research.deepbugs.common.model

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import org.jetbrains.research.deepbugs.common.DeepBugsPlugin
import org.jetbrains.research.deepbugs.common.ide.fus.collectors.counter.ErrorInfoCollector
import org.jetbrains.research.deepbugs.common.utils.Cbor
import org.jetbrains.research.deepbugs.common.utils.Mapping
import org.tensorflow.SavedModelBundle
import org.tensorflow.Session
import java.io.File
import java.lang.Exception

object ModelManager : StartupActivity, DumbAware {
    private val modelFolder by lazy { File(DeepBugsPlugin.installationFolder, "models") }

    var storage: ModelStorage? = null
        private set

    override fun runActivity(project: Project) {
        storage = ModelStorage(
            binOperandModel = loadModel("binOperandDetectionModel")!!,
            binOperatorModel = loadModel("binOperatorDetectionModel")!!,
            swappedArgsModel = loadModel("swappedArgsDetectionModel")!!,
            nodeTypeMapping = loadMapping("nodeTypeToVector.cbor"),
            typeMapping = loadMapping("typeToVector.cbor"),
            operatorMapping = loadMapping("operatorToVector.cbor"),
            tokenMapping = loadMapping("tokenToVector.cbor")
        )
    }

    private fun loadMapping(name: String): Mapping = Cbor.parse(File(modelFolder, name).readBytes(), Mapping.serializer())

    private fun loadModel(name: String): Session? = try {
        SavedModelBundle.load(File(modelFolder, name).canonicalPath, "serve").session()
    } catch (ex: Exception) {
        ErrorInfoCollector.logInitErrorReported()
        null
    }
}
