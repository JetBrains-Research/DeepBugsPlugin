package org.jetbrains.research.deepbugs.common.datatypes

interface DataType {
    val text: String

    fun vectorize(): List<Float>?
}
