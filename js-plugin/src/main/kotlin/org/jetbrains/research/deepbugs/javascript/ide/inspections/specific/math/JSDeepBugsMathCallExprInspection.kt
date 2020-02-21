package org.jetbrains.research.deepbugs.javascript.ide.inspections.specific.math

import com.intellij.lang.javascript.library.JSLibraryUtil
import com.intellij.lang.javascript.psi.JSCallExpression
import com.intellij.lang.javascript.psi.JSReferenceExpression
import com.intellij.psi.PsiElement
import org.jetbrains.research.deepbugs.common.ide.inspections.DeepBugsInspectionManager
import org.jetbrains.research.deepbugs.javascript.ide.inspections.base.JSDeepBugsCallExprInspection
import org.jetbrains.research.deepbugs.common.ide.inspections.specific.SpecificInspectionDescriptor

abstract class JSDeepBugsMathCallExprInspection : JSDeepBugsCallExprInspection() {
    init {
        DeepBugsInspectionManager.register(
            SpecificInspectionDescriptor { (it is JSReferenceExpression) && !skip(it) }
        )
    }

    protected open val ignore: List<String> = emptyList()

    override fun skip(node: PsiElement): Boolean {
        if (node !is JSCallExpression || node.arguments.size != requiredArgumentsNum) return true
        val call = node.methodExpression as? JSReferenceExpression ?: return true
        return ignore.contains(call.referenceName) || !call.isBuiltIn() && !call.isLibCall()
    }

    companion object {
        private val libsToConsider: List<String> = listOf("mathjs")
        private val modulesToConsider: List<String> = listOf("Math")

        private fun JSReferenceExpression.isBuiltIn(): Boolean {
            val first = qualifier?.text?.split('.')?.firstOrNull() ?: return false
            return modulesToConsider.contains(first)
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
    }
}
