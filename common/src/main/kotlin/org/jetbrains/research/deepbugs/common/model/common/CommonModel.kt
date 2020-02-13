package org.jetbrains.research.deepbugs.common.model.common

import org.jetbrains.research.deepbugs.common.model.ModelHandler
import org.jetbrains.research.keras.runner.nn.model.sequential.Perceptron

data class CommonModel(
    val binOperandModel: Perceptron = ModelHandler.loadModel("binOperandDetectionModel.h5", "common")!!,
    val binOperatorModel: Perceptron = ModelHandler.loadModel("binOperatorDetectionModel.h5", "common")!!,
    val swappedArgsModel: Perceptron = ModelHandler.loadModel("swappedArgsDetectionModel.h5", "common")!!
)
