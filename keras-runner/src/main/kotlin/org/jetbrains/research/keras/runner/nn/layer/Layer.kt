package org.jetbrains.research.keras.runner.nn.layer

import org.jetbrains.research.keras.runner.deserializer.json.LayerConfig
import org.jetbrains.research.keras.runner.deserializer.json.LayerConfigWrapper
import org.jetbrains.research.keras.runner.nn.activation.ActivationFunction
import org.jetbrains.research.keras.runner.nn.layer.dense.DenseLayer
import scientifik.kmath.structures.NDStructure

interface Layer<T : NDStructure<*>> {
    val name: String
    val parameters: LayerParameters<T>

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun createLayer(config: LayerConfigWrapper, params: LayerParameters<*>) = when (config.type) {
            LayerConfigWrapper.Type.DENSE -> DenseLayer(
                name = config.config.name,
                parameters = params as DenseLayerParameters,
                activationFunction = ActivationFunction.createActivationFunction((config.config as LayerConfig.Dense).activation)
            )
            LayerConfigWrapper.Type.DROPOUT -> null
        }
    }
}
