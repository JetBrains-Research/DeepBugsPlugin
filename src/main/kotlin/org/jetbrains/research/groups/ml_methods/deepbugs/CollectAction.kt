package org.jetbrains.research.groups.ml_methods.deepbugs

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiManager
import org.jetbrains.research.groups.ml_methods.deepbugs.datatypes.BinOp
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.toJson
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

    companion object {

        private const val BIN_OPS_PER_FILE = 30000

        private fun save(encounteredOperations: MutableList<BinOp>, saveRoot: String) {
            File("$saveRoot/binOps_${System.currentTimeMillis()}.json").writeText(toJson(encounteredOperations))
        }
    }

    override fun actionPerformed(e: AnActionEvent?) {
        if (e == null) return
        val project = e.project ?: return
        val root = project.basePath + "/data/python/python_all"
        val saveRoot = project.basePath + "/data/tech/idea"
        val encounteredOperations = mutableListOf<BinOp>()
        var totalOps = 0
        var totalFiles = 0
        File(root).walkTopDown().forEach {
            if (it.isFile && it.extension == "py") {
                println("Collecting from ${it.absolutePath}")
                val file = LocalFileSystem.getInstance().findFileByIoFile(it) ?: return
                val psiFile =
                        PsiManager.getInstance(ProjectManager.getInstance().defaultProject).findFile(file) ?: return
                val document = psiFile.viewProvider.document
                if (psiFile is PyFile) {
                    totalFiles++
                    Visitor(encounteredOperations, it.absolutePath, document).visitPyFile(psiFile)
                } else {
                    showErrorDialog("Oops, something is wrong with ${it.absolutePath}", project)
                }
                if (encounteredOperations.size > BIN_OPS_PER_FILE) {
                    totalOps += encounteredOperations.size
                    save(encounteredOperations, saveRoot)
                    encounteredOperations.clear()
//                    showInfoDialog("Processed $totalFiles files, $totalOps operations found so far", project)
                }
            }
        }
        println("Finished")
        totalOps += encounteredOperations.size
        save(encounteredOperations, saveRoot)
        showInfoDialog("Collected $totalOps operations from $totalFiles files.", project)
    }

    class Visitor(private val encounteredOperations: MutableList<BinOp>,
                  private val path: String,
                  private val document: Document?) : PyRecursiveElementVisitor() {

        override fun visitPyBinaryExpression(node: PyBinaryExpression?) {
            node?.let {
                val start = document?.getLineNumber(node.textRange.startOffset) ?: 0
                val end = document?.getLineNumber(node.textRange.endOffset) ?: 0
                BinOp.collectFromPyNode(it, "$path : $start - $end")?.let { binOp ->
                    encounteredOperations.add(binOp)
                }
            }
            super.visitPyBinaryExpression(node)
        }
    }

}