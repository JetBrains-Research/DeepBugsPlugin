package com.jetbrains.bogomolov.util

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

fun getProjectFor(event: AnActionEvent): Project? = event.getData(PlatformDataKeys.PROJECT)

fun getEditorFor(event: AnActionEvent): Editor? = event.getData(PlatformDataKeys.EDITOR)

fun getPsiFileFor(event: AnActionEvent): PsiFile? = event.getData(LangDataKeys.PSI_FILE)