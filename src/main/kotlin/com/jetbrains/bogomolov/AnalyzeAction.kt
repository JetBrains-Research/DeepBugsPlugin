package com.jetbrains.bogomolov

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.jetbrains.bogomolov.util.getEditorFor
import com.jetbrains.bogomolov.util.getProjectFor
import com.jetbrains.bogomolov.util.getPsiFileFor
import com.jetbrains.bogomolov.util.showInfoDialog


class AnalyzeAction : AnAction("Hello") {
    override fun actionPerformed(e: AnActionEvent) {
        val project = getProjectFor(e) ?: return
        val editor = getEditorFor(e)
        if (editor == null) {
            showInfoDialog("Source code editor is not selected", project)
            return
        }
        val psiFile = getPsiFileFor(e)
        if (psiFile == null) {
            showInfoDialog("No enclosing file found", project)
            return
        }
        
    }

    override fun update(e: AnActionEvent) {
        val project = e.project
        e.presentation.isEnabledAndVisible = project != null
    }
}