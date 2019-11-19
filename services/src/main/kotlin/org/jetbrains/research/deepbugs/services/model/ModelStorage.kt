package org.jetbrains.research.deepbugs.services.model

import org.jetbrains.research.deepbugs.services.utils.Mapping
import org.tensorflow.Session

data class ModelStorage(
    val nodeTypeMapping: Mapping,
    val typeMapping: Mapping,
    val operatorMapping: Mapping,
    val tokenMapping: Mapping,
    val binOperandModel: Session,
    val binOperatorModel: Session,
    val swappedArgsModel: Session
)
