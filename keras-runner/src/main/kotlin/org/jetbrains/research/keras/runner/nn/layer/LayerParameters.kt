package org.jetbrains.research.keras.runner.nn.layer

import scientifik.kmath.linear.RealMatrix
import scientifik.kmath.structures.NDStructure

open class LayerParameters<T : NDStructure<*>>(val weights: T?, val biases: T?)

typealias DenseLayerParameters = LayerParameters<RealMatrix>
