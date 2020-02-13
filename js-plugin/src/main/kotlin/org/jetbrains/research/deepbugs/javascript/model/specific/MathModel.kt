package org.jetbrains.research.deepbugs.javascript.model.specific

import org.jetbrains.research.deepbugs.common.model.ModelHandler
import org.jetbrains.research.keras.runner.nn.model.sequential.Perceptron

data class MathModel(
    val swappedArgsModel: Perceptron = ModelHandler.loadModel("swappedArgsDetectionModelMath.h5", "math")!!
)
