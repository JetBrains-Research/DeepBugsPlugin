package org.jetbrains.research.keras.runner.deserializer

import io.jhdf.HdfFile
import io.jhdf.api.Dataset
import org.jetbrains.research.keras.runner.deserializer.json.ModelConfig
import org.jetbrains.research.keras.runner.deserializer.json.ModelScheme
import org.jetbrains.research.keras.runner.nn.layer.Layer
import org.jetbrains.research.keras.runner.nn.layer.LayerParameters
import org.jetbrains.research.keras.runner.nn.model.sequential.Perceptron
import org.jetbrains.research.keras.runner.nn.model.sequential.SequentialModel
import java.io.File

@Suppress("UNCHECKED_CAST")
object ModelLoader {
    fun loadPerceptronModel(modelFile: File): Perceptron {
        val (config, layers) = importSequentialModelParameters(modelFile)
        config as ModelConfig.Sequential
        return SequentialModel.createPerceptron(config.name, layers, config.batchInputShape)
    }

    private fun importSequentialModelParameters(modelFile: File): Pair<ModelConfig, List<Layer<*>>> {
        return HdfFile(modelFile).let { hdf ->
            val config = importModelConfig(hdf)

            val layers = importSequentialModelLayers(hdf, config.config)

            config.config to layers
        }
    }

    private fun importSequentialModelLayers(hdf: HdfFile, config: ModelConfig) = config.layers.mapNotNull { layer ->
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

    private fun importModelConfig(hdf: HdfFile): ModelScheme {
        val configString = hdf.getAttribute("model_config").data as String
        return ModelScheme.parse(configString)
    }
}

