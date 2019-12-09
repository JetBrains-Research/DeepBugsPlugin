package org.jetbrains.research.keras.runner.nn.layer.dense

import org.jetbrains.research.keras.runner.nn.activation.ActivatableVector
import org.jetbrains.research.keras.runner.nn.activation.ActivationFunction
import org.jetbrains.research.keras.runner.nn.layer.ActivatableLayer
import scientifik.kmath.linear.BufferMatrix
import scientifik.kmath.linear.Point
import scientifik.kmath.structures.Matrix

class DenseLayer(
    name: String,
    params: DenseParameters,
    override val activationFunction: ActivationFunction
) : ActivatableLayer<Matrix<Double>>(name, params) {

    lateinit var inputArray: Point<Double>
    var outputArray: ActivatableVector = ActivatableVector(params.weights?.colNum ?: 0)

    override fun activate() {
        outputArray.forward(
            weights = params.weights!!,
            bias = params.biases,
            x = BufferMatrix(inputArray.size, 1, inputArray)
        )
        outputArray.activate(activationFunction)
    }

}
