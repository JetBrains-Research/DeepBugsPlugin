package org.jetbrains.research.deepbugs.javascript.ide.inspections

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.research.deepbugs.common.model.ModelManager
import org.jetbrains.research.deepbugs.javascript.JSDeepBugsConfig
import org.jetbrains.research.deepbugs.javascript.JSResourceBundle
import org.jetbrains.research.deepbugs.javascript.ide.inspections.base.JSDeepBugsCallExprInspection
import org.tensorflow.Session

class JSDeepBugsSwappedArgsInspection : JSDeepBugsCallExprInspection() {
    override val model: Session?
        get() = ModelManager.storage.swappedArgsModel
    override val threshold: Float
        get() = JSDeepBugsConfig.get().swappedArgsThreshold

    override fun createVisitor(
        holder: ProblemsHolder,
        session: LocalInspectionToolSession
    ): PsiElementVisitor = object : JSDeepBugsCallVisitor(holder, session) {
        override fun msg(node: NavigatablePsiElement): String =
            JSResourceBundle.message("deepbugs.javascript.swapped.args.inspection.warning")
    }

    override fun getDisplayName() = JSResourceBundle.message("deepbugs.javascript.swapped.args.inspection.display")
    override fun getShortName() = "JSCallInspection"
}
