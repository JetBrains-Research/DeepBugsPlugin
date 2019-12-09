package org.jetbrains.research.keras.runner.nn.layer

import org.jetbrains.research.keras.runner.nn.activation.ActivationFunction
import scientifik.kmath.structures.NDStructure

abstract class ActivatableLayer<T : NDStructure<*>>(name: String, params: Parameters<T>) : Layer<T>(name, params) {
    protected abstract val activationFunction: ActivationFunction

    abstract fun activate()
}
