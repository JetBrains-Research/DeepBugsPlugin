package org.jetbrains.research.deepbugs.common.model

import org.jetbrains.research.deepbugs.common.utils.Mapping

data class Vocabulary(
    val tokens: Mapping = ModelHandler.loadMapping("tokenToVector.cbor"),
    val operators: Mapping = ModelHandler.loadMapping("operatorToVector.cbor"),
    val types: Mapping = ModelHandler.loadMapping("typeToVector.cbor"),
    val nodeTypes: Mapping = ModelHandler.loadMapping("nodeTypeToVector.cbor")
)
