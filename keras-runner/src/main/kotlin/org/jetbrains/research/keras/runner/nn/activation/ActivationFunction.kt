package org.jetbrains.research.keras.runner.nn.activation

import org.jetbrains.research.keras.runner.deserializer.json.ActivationType
import scientifik.kmath.linear.BufferMatrix
import scientifik.kmath.linear.Point
import scientifik.kmath.linear.RealMatrix
import scientifik.kmath.structures.asBuffer
import scientifik.kmath.structures.asIterable
import kotlin.math.exp

sealed class ActivationFunction(private val func: (Double) -> Double) {
    operator fun invoke(array: Point<Double>): RealMatrix {
        return BufferMatrix(array.size, 1, array.asIterable().map(func).asBuffer())
    }

    object ReLU : ActivationFunction(func = { x -> if (x <= 0.0) 0.0 else x })
    object Sigmoid : ActivationFunction(func = { x -> 1.0 / (1.0 + exp(-x)) })

    companion object {
        operator fun get(type: ActivationType) = when (type) {
            ActivationType.relu -> ReLU
            ActivationType.sigmoid -> Sigmoid
        }
    }
}
