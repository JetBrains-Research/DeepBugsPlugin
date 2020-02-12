package org.jetbrains.research.deepbugs.common.ide.quickfixes

import com.intellij.codeInsight.intention.PriorityAction
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.lookup.LookupManager
import com.intellij.codeInspection.IntentionAndQuickFixAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import org.jetbrains.research.deepbugs.common.CommonResourceBundle
import org.jetbrains.research.deepbugs.common.datatypes.BinOp
import org.jetbrains.research.deepbugs.common.model.ModelStorage
import org.jetbrains.research.deepbugs.common.model.Vocabulary
import kotlin.math.max
import kotlin.math.min

class ReplaceBinOperatorQuickFix(
    private val data: BinOp,
    private val operatorRange: TextRange,
    private val threshold: Float,
    private val displayName: String,
    private val transform: (String) -> String = { it }
) : IntentionAndQuickFixAction(), PriorityAction {
    override fun getName(): String = if (lookups.size == 1) {
        CommonResourceBundle.message("deepbugs.replace.operator.single.quickfix", lookups.single().lookupString)
    } else {
        CommonResourceBundle.message("deepbugs.replace.operator.multiple.quickfix")
    }

    override fun getFamilyName(): String = displayName

    override fun getPriority(): PriorityAction.Priority = PriorityAction.Priority.HIGH

    override fun startInWriteAction(): Boolean = true

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean = lookups.isNotEmpty()

    override fun applyFix(project: Project, file: PsiFile, editor: Editor?) {
        editor ?: return

        val endOff = min(operatorRange.endOffset, editor.document.textLength)

        if (lookups.size == 1) {
            val lookup = lookups.single().lookupString
            val newEnd = operatorRange.startOffset + lookup.length
            editor.document.replaceString(operatorRange.startOffset, max(endOff, newEnd), lookup)
            return
        }

        editor.selectionModel.setSelection(operatorRange.startOffset, endOff)
        LookupManager.getInstance(project).showLookup(editor, *lookups.toTypedArray())
    }

    val lookups: List<LookupElementBuilder> by lazy {
        Vocabulary["operatorToVector"]!!.data.map {
            val newBinOp = data.replaceOperator(it.key).vectorize()
            val res = newBinOp?.let { op -> ModelStorage["binOperatorDetectionModel"]?.predict(op) }
            it.key to res
        }.filter { it.second != null && it.second!! < threshold }
            .sortedBy { it.second }.take(5)
            .map { LookupElementBuilder.create(transform(it.first)) }
    }
}
