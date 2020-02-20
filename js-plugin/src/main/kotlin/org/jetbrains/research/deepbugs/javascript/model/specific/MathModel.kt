package org.jetbrains.research.deepbugs.javascript.model.specific

import org.jetbrains.research.deepbugs.common.model.ModelHandler
import org.jetbrains.research.keras.runner.nn.model.sequential.Perceptron

data class MathModel(
    val swappedArgsModel: Perceptron = loadModel("swappedArgsDetectionModelMath.h5"),
    val incorrectArgModel: Perceptron = loadModel("incorrectFuncArgDetectionModelMath.h5")
) {
    companion object {
        private fun loadModel(model: String): Perceptron = ModelHandler.loadModel(model, "math")!!
    }
}
