package org.jetbrains.research.deepbugs.javascript.ide.inspections.specific.math

import com.intellij.lang.javascript.library.JSLibraryUtil
import com.intellij.lang.javascript.psi.JSCallExpression
import com.intellij.lang.javascript.psi.JSReferenceExpression
import org.jetbrains.research.deepbugs.common.ide.inspections.DeepBugsInspectionManager
import org.jetbrains.research.deepbugs.common.ide.inspections.specific.SpecificInspectionDescriptor
import org.jetbrains.research.deepbugs.javascript.ide.inspections.base.JSDeepBugsCallExprInspection

abstract class JSDeepBugsMathCallExprInspection(requiredArgsNum: Int, threshold: Float = 0.8f) : JSDeepBugsCallExprInspection(requiredArgsNum, threshold) {
    init {
        DeepBugsInspectionManager.register(
            SpecificInspectionDescriptor { (it is JSCallExpression) && !skip(it) }
        )
    }

    protected open val libsToConsider: Set<String> = emptySet()
    protected open val ignore: Set<String> = emptySet()

    override fun skip(node: JSCallExpression): Boolean {
        if (node.arguments.size != requiredArgumentsNum) return true
        val call = node.methodExpression as? JSReferenceExpression ?: return true
        return ignore.contains(call.referenceName) || (!call.isBuiltIn() && !call.isLibCall())
    }

    private fun JSReferenceExpression.isLibCall(): Boolean {
        val resolvedFiles = try {
            multiResolve(false).mapNotNull { it.element?.containingFile }
        } catch (ex: Exception) {
            return false
        }
        val libs = resolvedFiles.mapNotNull { JSLibraryUtil.getLibraryFolder(it.virtualFile)?.name }
        return libsToConsider.intersect(libs).isNotEmpty()
    }

    companion object {
        private val modulesToConsider: Set<String> = setOf("Math")

        private fun JSReferenceExpression.isBuiltIn(): Boolean {
            val first = qualifier?.text?.split('.')?.firstOrNull() ?: return false
            return modulesToConsider.contains(first)
        }
    }
}
