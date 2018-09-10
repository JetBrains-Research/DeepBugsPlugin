package com.jetbrains.bogomolov

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.bogomolov.extraction.extractPyNodeName
import com.jetbrains.bogomolov.extraction.extractPyNodeType
import com.jetbrains.python.inspections.PyInspection
import com.jetbrains.python.inspections.PyInspectionVisitor
import com.jetbrains.python.psi.PyBinaryExpression
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport

class DeepBugsInspection : PyInspection() {

    override fun buildVisitor(
            holder: ProblemsHolder,
            isOnTheFly: Boolean,
            session: LocalInspectionToolSession
    ): PsiElementVisitor = Visitor(holder, session)

    class Visitor(holder: ProblemsHolder, session: LocalInspectionToolSession) : PyInspectionVisitor(holder, session) {

        override fun visitPyBinaryExpression(node: PyBinaryExpression?) {
            if (node == null) {
                super.visitPyBinaryExpression(node)
                return
            }
            val leftName = extractPyNodeName(node.leftExpression)
            val rightName = extractPyNodeName(node.rightExpression)
            val leftType = extractPyNodeType(node.leftExpression)
            val rightType = extractPyNodeType(node.rightExpression)
            if (leftName != null && rightName != null) {
                registerProblem(node,
                        "LEFT: $leftName, RIGHT: $rightName",
                        ProblemHighlightType.GENERIC_ERROR_OR_WARNING)
            }
            super.visitPyBinaryExpression(node)
        }
    }

}