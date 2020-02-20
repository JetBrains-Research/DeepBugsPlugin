package org.jetbrains.research.deepbugs.common.ide.ui

import com.intellij.openapi.ui.panel.ComponentPanelBuilder
import java.awt.*
import javax.swing.JComponent
import javax.swing.JPanel

fun panel(layout: LayoutManager = BorderLayout(0, 0), body: JPanel.() -> Unit) = JPanel(layout).apply(body)

fun Container.panel(
    layout: LayoutManager = BorderLayout(0, 0), constraint: Any,
    body: JPanel.() -> Unit
): JPanel = JPanel(layout).apply(body).also { add(it, constraint) }

fun wrapWithComment(component: JComponent, comment: String) = ComponentPanelBuilder(component)
    .withComment(comment)
    .moveCommentRight()
    .resizeY(true)
    .createPanel()
