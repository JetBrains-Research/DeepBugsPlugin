package org.jetbrains.research.deepbugs.common.model.common

import org.jetbrains.research.deepbugs.common.model.ModelHandler
import org.jetbrains.research.keras.runner.nn.model.sequential.Perceptron

data class CommonModel(
    val binOperandModel: Perceptron = loadModel("binOperandDetectionModel.h5"),
    val binOperatorModel: Perceptron = loadModel("binOperatorDetectionModel.h5"),
    val swappedArgsModel: Perceptron = loadModel("swappedArgsDetectionModel.h5")
) {
    companion object {
        fun loadModel(model: String): Perceptron = ModelHandler.loadModel(model, "common")!!
    }
}
