package org.jetbrains.research.deepbugs.keras.runner.deserializer

import scientifik.kmath.linear.VirtualMatrix
import scientifik.kmath.linear.transpose

fun FloatArray.toDouble() = map { it.toDouble() }
fun Array<FloatArray>.toDouble() = map { it.toDouble() }

fun Any.toMatrix() = try {
    val data = cast<Array<FloatArray>>(this).toDouble()
    VirtualMatrix(rowNum = data.size, colNum = data[0].size, generator = { i, j -> data[i][j]}).transpose()
} catch (ex: Exception) {
    val data = cast<FloatArray>(this).toDouble()
    VirtualMatrix(rowNum = data.size, colNum = 1, generator = { i, _ -> data[i] }).transpose()
}

private inline fun <reified T> cast(obj: Any): T {
    check(obj is T) { "Expected ${T::class} but found ${obj::class}" }
    return obj
}
