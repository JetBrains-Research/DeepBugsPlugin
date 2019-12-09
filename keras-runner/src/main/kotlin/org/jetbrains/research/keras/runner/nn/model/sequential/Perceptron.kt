package org.jetbrains.research.keras.runner.nn.model.sequential

import org.jetbrains.research.keras.runner.nn.layer.dense.DenseLayer
import scientifik.kmath.linear.Point
import scientifik.kmath.linear.asPoint

open class Perceptron(
    name: String,
    override val layers: List<DenseLayer>,
    batchInputShape: List<Int?>
) : SequentialModel<List<Float>, Float>(name, layers, batchInputShape) {

    override fun predict(input: List<Float>): Float {
        initInput(input)

        layers.zipWithNext { prev, cur ->
            cur.inputArray = prev.outputArray.values.asPoint()
            cur.activate()
        }

        return layers.last().outputArray.values[0, 0].toFloat()
    }

    private fun initInput(input: List<Float>) {
        require(batchInputShape!!.filterNotNull().single() == input.size) { "Unmatched input shapes" }

        layers.first().let {
            it.inputArray = Point.real(input.size) { i -> input[i].toDouble() }
            it.activate()
        }
    }
}
