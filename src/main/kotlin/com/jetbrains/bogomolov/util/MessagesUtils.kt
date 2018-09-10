package com.jetbrains.bogomolov.util

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
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