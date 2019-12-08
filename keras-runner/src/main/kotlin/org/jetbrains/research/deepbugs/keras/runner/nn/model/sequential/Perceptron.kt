package org.jetbrains.research.deepbugs.keras.runner.nn.model.sequential

import org.jetbrains.research.deepbugs.keras.runner.nn.layer.dense.DenseLayer
import scientifik.kmath.linear.Point
import scientifik.kmath.linear.asPoint

open class Perceptron(
    name: String,
    override val layers: List<DenseLayer>,
    batchInputShape: List<Int?>
) : SequentialModel<List<Float>, Float>(name, layers, batchInputShape) {

    override fun predict(input: List<Float>?): Float {
        input ?: return 0.0f

        require(batchInputShape!!.filterNotNull().single() == input.size) { "Unmatched input shapes" }

        layers.first().inputArray = Point.real(input.size) { input[it].toDouble() }
        var prevLayer: DenseLayer? = null
        for (layer in layers) {
            if (prevLayer != null) layer.inputArray = prevLayer.outputArray.values.asPoint()

            layer.activate()
            prevLayer = layer
        }

        return layers.last().outputArray.values[0, 0].toFloat()
    }
}
