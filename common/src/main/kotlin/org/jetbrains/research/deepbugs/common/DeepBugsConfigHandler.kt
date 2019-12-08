package org.jetbrains.research.deepbugs.common

import org.jetbrains.research.deepbugs.common.datatypes.DataType

abstract class DeepBugsConfigHandler {
    protected abstract val instance: DeepBugsConfig
    abstract val default: DeepBugsConfig.State

    fun get() = instance.state

    @Synchronized
    fun ignoreExpression(expr: DataType) = instance.disableCheck(expr.text)

    @Synchronized
    fun considerExpression(expr: DataType) = instance.enableCheck(expr.text)

    fun shouldIgnore(expr: DataType) = get().userDisabledChecks.contains(expr.text)

    fun isProblem(result: Float, threshold: Float, expr: DataType) = result > threshold && !shouldIgnore(expr)
}
