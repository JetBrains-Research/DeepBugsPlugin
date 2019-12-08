package org.jetbrains.research.deepbugs.keras.runner.nn.model

interface Model<in T, out V> {
    val name: String

    fun predict(input: T?): V?
}
