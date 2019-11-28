package org.jetbrains.research.deepbugs.common.ide.ui

import com.intellij.openapi.options.ConfigurableUi
import com.intellij.ui.layout.migLayout.createLayoutConstraints
import com.intellij.util.ui.JBUI
import net.miginfocom.layout.AC
import net.miginfocom.layout.CC
import net.miginfocom.swing.MigLayout
import org.jdesktop.swingx.VerticalLayout
import org.jetbrains.research.deepbugs.common.CommonResourceBundle
import org.jetbrains.research.deepbugs.common.DeepBugsConfig
import org.jetbrains.research.deepbugs.common.ide.msg.DeepBugsLifecycle
import javax.swing.JButton

class DeepBugsSettingsPanel(
    private val settings: DeepBugsConfig,
    private val default: DeepBugsConfig.State,
    private val displayName: String
) : ConfigurableUi<DeepBugsConfig> {
    companion object: DeepBugsLifecycle {
        private val resetButton = JButton(CommonResourceBundle.message("reset.button.text"))

        override fun init(init: DeepBugsConfig.State) {
            resetButton.isEnabled = init.userDisabledChecks.isNotEmpty()
        }

        override fun update(previous: DeepBugsConfig.State, new: DeepBugsConfig.State) {
            if (previous.userDisabledChecks == new.userDisabledChecks) return

            init(new)
        }
    }

    init {
        resetButton.addActionListener {
            settings.update(default)
        }
    }


    override fun getComponent() = panel(MigLayout(createLayoutConstraints(), AC().grow(), AC().index(1).grow())) {
        panel(MigLayout(createLayoutConstraints(), AC().grow()), constraint = CC().growX().wrap()) {
            border = border(CommonResourceBundle.message("reset.button.comment", displayName), false, JBUI.insetsBottom(10), false)
            add(resetButton, CC().growX().width("20%").height("10%").alignY("top"))
            panel(VerticalLayout(), CC().grow().width("80%").alignY("top")) {
                border = padding(JBUI.insetsLeft(20))
            }
        }
    }

    override fun apply(settings: DeepBugsConfig) = Unit
    override fun isModified(settings: DeepBugsConfig): Boolean = false
    override fun reset(settings: DeepBugsConfig) = Unit
}
