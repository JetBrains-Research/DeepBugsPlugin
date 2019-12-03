package org.jetbrains.research.deepbugs.javascript.ide.quickfixes

import com.intellij.openapi.util.TextRange
import org.jetbrains.research.deepbugs.common.datatypes.BinOp
import org.jetbrains.research.deepbugs.common.ide.quickfixes.ReplaceBinOperatorQuickFix
import org.jetbrains.research.deepbugs.javascript.JSResourceBundle
import org.jetbrains.research.deepbugs.javascript.ide.quickfixes.utils.operators

class JSReplaceBinOperatorQuickFix(data: BinOp, operatorRange: TextRange, threshold: Float) : ReplaceBinOperatorQuickFix(data, operatorRange, threshold) {
    override val transformBinOp: (String) -> String = { operators[it] ?: "" }

    override fun getFamilyName(): String = JSResourceBundle.message("deepbugs.javascript.display")
}
