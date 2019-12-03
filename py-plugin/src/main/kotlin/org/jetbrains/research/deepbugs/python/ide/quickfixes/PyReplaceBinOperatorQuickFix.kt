package org.jetbrains.research.deepbugs.python.ide.quickfixes

import com.intellij.openapi.util.TextRange
import org.jetbrains.research.deepbugs.common.datatypes.BinOp
import org.jetbrains.research.deepbugs.common.ide.quickfixes.ReplaceBinOperatorQuickFix
import org.jetbrains.research.deepbugs.python.PyResourceBundle

class PyReplaceBinOperatorQuickFix(data: BinOp, operatorRange: TextRange, threshold: Float) : ReplaceBinOperatorQuickFix(data, operatorRange, threshold) {
    override fun getFamilyName(): String = PyResourceBundle.message("deepbugs.python.display")
}
