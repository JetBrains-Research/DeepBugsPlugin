package org.jetbrains.research.deepbugs.keras.runner.nn.model.sequential

import org.jetbrains.research.deepbugs.keras.runner.nn.layer.Layer
import org.jetbrains.research.deepbugs.keras.runner.nn.layer.dense.DenseLayer
import org.jetbrains.research.deepbugs.keras.runner.nn.model.Model

open class SequentialModel<in T, out V>(
        override val name: String,
        protected open val layers: List<Layer<*>>,
        val batchInputShape: List<Int?>? = null
) : Model<T, V> {

    override fun predict(input: T?): V? = null

    companion object {
        fun createPerceptron(name: String, layers: List<Layer<*>>, batchInputShape: List<Int?>?) : Perceptron {
            require(batchInputShape != null) { "Model input shape is unspecified" }
            require(batchInputShape.filterNotNull().size == 1) { "Input should be one-dimensional" }
            layers as List<DenseLayer>
            return Perceptron(name, layers, batchInputShape)
        }
    }
}
