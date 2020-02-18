package org.jetbrains.research.deepbugs.javascript.ide.inspections.specific.math

import com.intellij.lang.javascript.library.JSLibraryUtil
import com.intellij.lang.javascript.psi.JSCallExpression
import com.intellij.lang.javascript.psi.JSReferenceExpression
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElement
import org.jetbrains.research.deepbugs.javascript.ide.inspections.DeepBugsInspectionManager
import org.jetbrains.research.deepbugs.javascript.ide.inspections.base.JSDeepBugsCallExprInspection
import org.jetbrains.research.deepbugs.javascript.ide.inspections.specific.SpecificInspectionDescriptor

abstract class JSDeepBugsMathCallExprInspection : JSDeepBugsCallExprInspection() {
    init {
        DeepBugsInspectionManager.register(
            object : SpecificInspectionDescriptor {
                override fun shouldProcess(element: PsiElement): Boolean {
                    if (element !is JSReferenceExpression) return false
                    return !skip(element)
                }
            }
        )
    }

    override fun skip(node: NavigatablePsiElement): Boolean = (node as? JSCallExpression)?.methodExpression?.let {
        if (it !is JSReferenceExpression) return@let true
        node.arguments.size != requiredArgumentsNum || !it.isBuiltIn() && !it.isLibCall()
    } ?: true

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
