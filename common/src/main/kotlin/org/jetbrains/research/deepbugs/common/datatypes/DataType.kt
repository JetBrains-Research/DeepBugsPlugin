package org.jetbrains.research.deepbugs.common.datatypes

abstract class DataType {
    abstract val text: String
    abstract fun vectorize(): FloatArray?
}
