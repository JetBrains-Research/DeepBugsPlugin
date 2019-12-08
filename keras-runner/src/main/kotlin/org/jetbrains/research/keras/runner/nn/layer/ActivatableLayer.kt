package org.jetbrains.research.keras.runner.nn.layer

import org.jetbrains.research.keras.runner.nn.activation.ActivationFunction
import scientifik.kmath.structures.NDStructure

abstract class ActivatableLayer<T : NDStructure<*>>(override val name: String) : Layer<T> {
    protected abstract val activationFunction: ActivationFunction

    abstract fun activate()
}
