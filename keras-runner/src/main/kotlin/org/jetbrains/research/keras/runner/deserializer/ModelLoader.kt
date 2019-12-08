package org.jetbrains.research.keras.runner.deserializer

import io.jhdf.HdfFile
import io.jhdf.api.Dataset
import org.jetbrains.research.keras.runner.deserializer.json.ModelConfig
import org.jetbrains.research.keras.runner.deserializer.json.ModelConfigWrapper
import org.jetbrains.research.keras.runner.nn.layer.Layer
import org.jetbrains.research.keras.runner.nn.layer.LayerParameters
import org.jetbrains.research.keras.runner.nn.model.sequential.Perceptron
import org.jetbrains.research.keras.runner.nn.model.sequential.SequentialModel
import java.io.File

@Suppress("UNCHECKED_CAST")
object ModelLoader {
    fun importSequentialModelAndWeights(modelFile: File): Pair<ModelConfig, List<Layer<*>>> {
        return HdfFile(modelFile).let { hdf ->
            val configString = hdf.getAttribute("model_config").data as String
            val config = ModelConfigWrapper.parse(configString)

            val layers = config.config.layers.mapNotNull { layer ->
                val layerWeights = hdf.getByPath("model_weights/${layer.config.name}")
                val weightNames =
                    (layerWeights.getAttribute("weight_names").data as? Array<String?>)?.filterNotNull()

                val (weights, biases) = weightNames?.map { weight ->
                    val data = hdf.getByPath("model_weights/${layer.config.name}/$weight") as Dataset
                    weight to data.data.toMatrix()
                }?.partition { !it.first.contains("bias") } ?: null to null

                val params = LayerParameters(
                    weights?.map { it.second }?.singleOrNull(),
                    biases?.map { it.second }?.singleOrNull()
                )
                Layer.createLayer(layer, params)
            }

            config.config to layers
        }
    }

    fun loadPerceptronModel(modelFile: File): Perceptron {
        val (config, layers) = importSequentialModelAndWeights(modelFile)
        config as ModelConfig.Sequential
        return SequentialModel.createPerceptron(config.name, layers, config.batchInputShape)
    }
}

