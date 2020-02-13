package org.jetbrains.research.deepbugs.common.model

import org.jetbrains.research.deepbugs.common.DeepBugsPlugin
import org.jetbrains.research.deepbugs.common.utils.Cbor
import org.jetbrains.research.deepbugs.common.utils.Mapping
import org.jetbrains.research.keras.runner.deserializer.ModelLoader
import org.jetbrains.research.keras.runner.nn.model.sequential.Perceptron
import java.io.File
import java.nio.file.Paths

object ModelHandler {
    private val modelsRoot by lazy { File(DeepBugsPlugin.installationFolder, "models") }

    private fun getModule(module: String) = File(modelsRoot, module)

    fun loadMapping(name: String): Mapping = Cbor.parse(File(modelsRoot, name).readBytes(), Mapping.serializer())

    fun loadModel(name: String, module: String): Perceptron? = try {
        ModelLoader.loadPerceptronModel(File(getModule(module), name))
    } catch (ex: Exception) {
        null
    }
}
