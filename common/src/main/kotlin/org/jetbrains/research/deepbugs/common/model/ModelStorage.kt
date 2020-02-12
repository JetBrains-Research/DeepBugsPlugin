package org.jetbrains.research.deepbugs.common.model

import org.jetbrains.research.deepbugs.common.DeepBugsPlugin
import org.jetbrains.research.keras.runner.deserializer.ModelLoader
import org.jetbrains.research.keras.runner.nn.model.sequential.Perceptron
import java.io.File

object ModelStorage : AbstractStorage<Perceptron>() {
    override val storage: HashMap<String, Perceptron> = hashMapOf()
    override val ext: String = "h5"

    override fun load(name: String): Perceptron? = storage.put(name, ModelLoader.loadPerceptronModel(File(DeepBugsPlugin.modelsFolder, name)))
}
