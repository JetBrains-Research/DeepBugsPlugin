package org.jetbrains.research.keras.runner.nn.model

abstract class Model<in T, out V>(val name: String) {
    abstract fun predict(input: T): V?
}
