package org.jetbrains.research.deepbugs.javascript.ide.inspections.specific.math

import com.intellij.lang.javascript.library.JSLibraryUtil
import com.intellij.lang.javascript.psi.JSCallExpression
import com.intellij.lang.javascript.psi.JSExpression
import com.intellij.lang.javascript.psi.JSReferenceExpression
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElement
import org.jetbrains.research.deepbugs.javascript.JSResourceBundle
import org.jetbrains.research.deepbugs.javascript.ide.inspections.DeepBugsInspectionManager
import org.jetbrains.research.deepbugs.javascript.ide.inspections.common.JSDeepBugsSwappedArgsInspection
import org.jetbrains.research.deepbugs.javascript.ide.inspections.specific.SpecificInspectionDescriptor
import org.jetbrains.research.deepbugs.javascript.model.JSModelStorage
import org.jetbrains.research.keras.runner.nn.model.sequential.Perceptron

class JSDeepBugsSwappedArgsMathInspection : JSDeepBugsSwappedArgsInspection() {
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

    override val model: Perceptron?
        get() = JSModelStorage.specific.math.swappedArgsModel

    override fun skip(node: NavigatablePsiElement): Boolean = (node as? JSCallExpression)?.methodExpression?.let {
        node.arguments.size != requiredArgumentsNum || !isBuiltIn(it) && !isLibCall(it)
    } ?: true

    override fun createTooltip(node: NavigatablePsiElement, vararg params: Any): String =
        JSResourceBundle.message("deepbugs.javascript.math.swapped.args.inspection.warning")

    private fun isBuiltIn(expr: JSExpression): Boolean {
        expr as JSReferenceExpression
        val first = expr.qualifier?.text?.split('.')?.firstOrNull() ?: return false
        return modulesToConsider.contains(first)
    }

    private fun isLibCall(expr: JSExpression): Boolean {
        if (expr !is JSReferenceExpression) return false
        val resolvedFiles = try {
            expr.multiResolve(false).mapNotNull { it.element?.containingFile }
        } catch (ex: Exception) {
            return false
        }
        val libs = resolvedFiles.mapNotNull { JSLibraryUtil.getLibraryFolder(it.virtualFile)?.name }
        return libsToConsider.intersect(libs).isNotEmpty()
    }

    override fun getShortName() = "JSDeepBugsSwappedArgsMath"

    companion object {
        private val libsToConsider: List<String> = listOf("mathjs")
        private val modulesToConsider: List<String> = listOf("Math")
    }
}
