package org.jetbrains.research.deepbugs.python.ide.quickfixes

import com.intellij.psi.PsiElement
import com.jetbrains.python.psi.PyCallExpression
import org.jetbrains.research.deepbugs.common.ide.quickfixes.FlipFunctionArgumentsQuickFix
import org.jetbrains.research.deepbugs.python.PyResourceBundle

class PyFlipFunctionArgumentsQuickFix : FlipFunctionArgumentsQuickFix<PyCallExpression>() {
    override fun getFamilyName(): String = PyResourceBundle.message("deepbugs.python.flip.args.family")

    override fun PyCallExpression.toArguments(): Pair<PsiElement, PsiElement> = Pair(arguments[0], arguments[1])
}
