package org.jetbrains.research.groups.ml_methods.deepbugs.services.datatypes

import org.tensorflow.Tensor

enum class NodeType(val nodeName: String, val nodeClass: Class<out DataType>) {
    BIN_OP("binOp", BinOp::class.java),
    CALL("call", Call::class.java)
}

interface DataType {
    fun vectorize(): Tensor<Float>?
}