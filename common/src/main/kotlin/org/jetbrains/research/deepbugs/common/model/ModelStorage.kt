package org.jetbrains.research.deepbugs.common.model

import org.jetbrains.research.deepbugs.keras.runner.nn.model.sequential.Perceptron
import org.jetbrains.research.deepbugs.common.utils.Mapping

data class ModelStorage(
    val nodeTypeMapping: Mapping,
    val typeMapping: Mapping,
    val operatorMapping: Mapping,
    val tokenMapping: Mapping,
    val binOperandModel: Perceptron,
    val binOperatorModel: Perceptron,
    val swappedArgsModel: Perceptron
)
