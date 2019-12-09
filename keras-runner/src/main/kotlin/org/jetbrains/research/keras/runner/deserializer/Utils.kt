package org.jetbrains.research.keras.runner.deserializer

import scientifik.kmath.linear.VirtualMatrix
import scientifik.kmath.linear.transpose
import scientifik.kmath.structures.Matrix
import java.lang.IllegalStateException

inline fun <reified T : Number> List<T>.toDoubleList(): List<Double> = map { it.toDouble() }

fun FloatArray.toDoubleList() = asList().toDoubleList()
fun Array<FloatArray>.toDoubleList() = map { it.toDoubleList() }

@Suppress("UNCHECKED_CAST")
inline fun <reified T> T.toMatrix(): Matrix<*> = when (this) {
    is FloatArray -> VirtualMatrix(rowNum = size, colNum = 1, generator = { i, _ -> this[i] }).transpose()
    is Array<*> -> {
        this as Array<FloatArray>
        VirtualMatrix(rowNum = size, colNum = this[0].size, generator = { i, j -> this[i][j] }).transpose()
    }
    else -> throw IllegalStateException("Cannot cast ${T::class} to matrix")
}
