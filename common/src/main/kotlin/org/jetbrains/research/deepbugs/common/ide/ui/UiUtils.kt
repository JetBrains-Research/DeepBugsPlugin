package org.jetbrains.research.deepbugs.common.ide.ui

import com.intellij.ui.IdeBorderFactory
import java.awt.BorderLayout
import java.awt.Container
import java.awt.Insets
import java.awt.LayoutManager
import javax.swing.JPanel
import javax.swing.border.Border

fun panel(layout: LayoutManager = BorderLayout(0, 0), body: JPanel.() -> Unit) = JPanel(layout).apply(body)

fun Container.panel(layout: LayoutManager = BorderLayout(0, 0), constraint: Any,
    body: JPanel.() -> Unit): JPanel = JPanel(layout).apply(body).also { add(it, constraint) }

fun border(text: String, hasIndent: Boolean, insets: Insets, showLine: Boolean = true): Border = IdeBorderFactory.createTitledBorder(text, hasIndent, insets).setShowLine(showLine)

fun padding(insets: Insets): Border = IdeBorderFactory.createEmptyBorder(insets)
