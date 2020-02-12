package org.jetbrains.research.deepbugs.common.model

abstract class AbstractStorage<T> {
    protected open val storage: Map<String, T> = emptyMap()

    abstract val ext: String
    abstract fun load(name: String): T?

    operator fun get(name: String): T? = storage[name] ?: load("$name.$ext")
}
