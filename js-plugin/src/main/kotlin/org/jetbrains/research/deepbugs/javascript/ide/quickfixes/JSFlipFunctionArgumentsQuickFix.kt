package org.jetbrains.research.deepbugs.javascript.ide.quickfixes

import com.intellij.lang.javascript.psi.JSCallExpression
import com.intellij.psi.PsiElement
import org.jetbrains.research.deepbugs.common.ide.quickfixes.FlipFunctionArgumentsQuickFix
import org.jetbrains.research.deepbugs.javascript.JSResourceBundle

class JSFlipFunctionArgumentsQuickFix : FlipFunctionArgumentsQuickFix<JSCallExpression>() {
    override fun getFamilyName(): String = JSResourceBundle.message("deepbugs.javascript.flip.args.family")

    override fun JSCallExpression.toArguments(): Pair<PsiElement, PsiElement> = Pair(arguments[0], arguments[1])
}
