package org.jetbrains.research.deepbugs.common.model

abstract class AbstractStorage<T> {
    protected open val storage: Map<String, T> = emptyMap()

    protected abstract val ext: String
    protected abstract fun load(name: String): T?

    operator fun get(name: String): T? = storage[name] ?: load("$name.$ext")
}
