package org.jetbrains.research.keras.runner.nn.layer.dense

import org.jetbrains.research.keras.runner.nn.activation.ActivatableVector
import org.jetbrains.research.keras.runner.nn.activation.ActivationFunction
import org.jetbrains.research.keras.runner.nn.layer.ActivatableLayer
import org.jetbrains.research.keras.runner.nn.layer.DenseLayerParameters
import scientifik.kmath.linear.BufferMatrix
import scientifik.kmath.linear.Point
import scientifik.kmath.structures.Matrix

class DenseLayer(
    name: String,
    override val parameters: DenseLayerParameters,
    override val activationFunction: ActivationFunction
) : ActivatableLayer<Matrix<Double>>(name) {

    lateinit var inputArray: Point<Double>
    var outputArray: ActivatableVector = ActivatableVector(parameters.weights?.colNum ?: 0)

    private val forwardHelper = DenseForwardHelper(this)

    override fun activate() = forwardHelper.forwardAndActivate()

    private inner class DenseForwardHelper(val layer: DenseLayer) {
        fun forwardAndActivate() {
            layer.outputArray.forward(
                weights = layer.parameters.weights!!,
                bias = layer.parameters.biases,
                x = BufferMatrix(layer.inputArray.size, 1, layer.inputArray)
            )

            layer.outputArray.activate(layer.activationFunction)
        }
    }
}
