package com.jetbrains.bogomolov

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiManager
import com.jetbrains.bogomolov.extraction.extractPyNodeName
import com.jetbrains.bogomolov.extraction.extractPyNodeType
import com.jetbrains.python.psi.*
import java.io.File
import javax.swing.Icon

fun showInfoDialog(message: String, project: Project) {
    showMessageDialog(project, message, "Information", Messages.getInformationIcon())
}

fun showErrorDialog(message: String, project: Project) {
    showMessageDialog(project, message, "Error", Messages.getErrorIcon())
}

private fun showMessageDialog(project: Project, message: String, title: String, icon: Icon) {
    Messages.showMessageDialog(project, message, title, icon)
}

class CollectAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent?) {
        if (e == null) return
        val project = e.project ?: return
        showInfoDialog(ProjectManager.getInstance().openProjects.size.toString(), project)
        val path = project.basePath + "/data/SampleFile.py"
        val file = LocalFileSystem.getInstance().findFileByIoFile(File(path)) ?: return
        val psiFile =
                PsiManager.getInstance(ProjectManager.getInstance().defaultProject).findFile(file) ?: return
        if (psiFile is PyFile) {
            val visitor = Visitor(project)
            visitor.visitPyFile(psiFile)
            showInfoDialog(visitor.encounteredOperations.toString(), project)
        }
    }

    class Visitor(private val project: Project) : PyRecursiveElementVisitor() {

        val encounteredOperations = mutableListOf<String>()

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
                encounteredOperations.add("LEFT: $leftName, RIGHT: $rightName")
                showInfoDialog("LEFT: $leftName, RIGHT: $rightName", project)
            }
            super.visitPyBinaryExpression(node)
        }
    }

}