package org.jetbrains.research.deepbugs.common.ide.quickfixes

import com.intellij.codeInsight.intention.PriorityAction
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.lookup.LookupManager
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import org.jetbrains.research.deepbugs.common.CommonResourceBundle
import org.jetbrains.research.deepbugs.common.datatypes.BinOp
import org.jetbrains.research.deepbugs.common.model.ModelManager
import kotlin.math.max
import kotlin.math.min

class ReplaceBinOperatorQuickFix(
    private val data: BinOp,
    private val operatorRange: TextRange,
    private val threshold: Float,
    private val displayName: String,
    private val transform: (String) -> String = { it }
) : LocalQuickFix, PriorityAction {
    override fun getName(): String = if (lookups.size == 1) {
        CommonResourceBundle.message("deepbugs.replace.operator.single.quickfix", lookups.single().lookupString)
    } else {
        CommonResourceBundle.message("deepbugs.replace.operator.multiple.quickfix")
    }

    override fun getFamilyName(): String = displayName

    override fun getPriority(): PriorityAction.Priority = PriorityAction.Priority.HIGH

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        DataManager.getInstance().dataContextFromFocusAsync.onSuccess { context ->
            val editor: Editor = CommonDataKeys.EDITOR.getData(context) ?: return@onSuccess

            val endOff = min(operatorRange.endOffset, editor.document.textLength)

            if (lookups.size == 1) {
                val lookup = lookups.single().lookupString
                editor.document.replaceString(operatorRange.startOffset, max(endOff, operatorRange.startOffset + lookup.length), lookup)
                return@onSuccess
            }

            editor.selectionModel.setSelection(operatorRange.startOffset, endOff)
            LookupManager.getInstance(project).showLookup(editor, *lookups.toTypedArray())
        }
    }

    val lookups: List<LookupElementBuilder> by lazy {
        ModelManager.storage.operatorMapping.data.map {
            val newBinOp = data.replaceOperator(it.key).vectorize()
            val res = newBinOp?.let { op -> ModelManager.storage.binOperatorModel.predict(op) }
            it.key to res
        }.filter { it.second != null && it.second!! < threshold }
            .sortedBy { it.second }.take(5)
            .map { LookupElementBuilder.create(transform(it.first)) }
    }
}
