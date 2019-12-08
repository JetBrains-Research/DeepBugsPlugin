package org.jetbrains.research.deepbugs.keras.runner.deserializer

import scientifik.kmath.linear.VirtualMatrix
import scientifik.kmath.linear.transpose
import scientifik.kmath.structures.Matrix
import java.lang.IllegalStateException

inline fun <reified T : Number> List<T>.toDoubleList(): List<Double> = map { it.toDouble() }

fun FloatArray.toDoubleList() = asList().toDoubleList()
fun Array<FloatArray>.toDoubleList() = map { it.toDoubleList() }

inline fun <reified T> T.toMatrix(): Matrix<*> = when(this) {
    is FloatArray -> VirtualMatrix(rowNum = size, colNum = 1, generator = { i, _ -> this[i] }).transpose()
    is Array<*> -> {
        val data = cast<Array<FloatArray>>(this)
        VirtualMatrix(rowNum = data.size, colNum = data[0].size, generator = { i, j -> data[i][j] }).transpose()
    }
    else -> throw IllegalStateException("Cannot cast ${T::class} to matrix")
}

inline fun <reified T> cast(obj: Any): T {
    check(obj is T) { "Expected ${T::class} but found ${obj::class}" }
    return obj
}
