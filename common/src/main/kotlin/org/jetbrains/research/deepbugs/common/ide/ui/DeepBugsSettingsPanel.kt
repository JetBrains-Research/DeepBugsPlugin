package org.jetbrains.research.deepbugs.common.ide.ui

import com.intellij.openapi.options.ConfigurableUi
import com.intellij.ui.layout.migLayout.createLayoutConstraints
import net.miginfocom.layout.AC
import net.miginfocom.layout.CC
import net.miginfocom.swing.MigLayout
import org.jetbrains.research.deepbugs.common.CommonResourceBundle
import org.jetbrains.research.deepbugs.common.DeepBugsConfig
import org.jetbrains.research.deepbugs.common.ide.msg.DeepBugsLifecycle
import javax.swing.JButton

class DeepBugsSettingsPanel(private val settings: DeepBugsConfig, private val default: DeepBugsConfig.State) : ConfigurableUi<DeepBugsConfig> {
    companion object : DeepBugsLifecycle {
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
            add(wrapWithComment(resetButton, CommonResourceBundle.message("reset.button.comment")),
                CC().growX().width("100%").height("10%").alignY("top"))
        }
    }

    override fun apply(settings: DeepBugsConfig) = Unit
    override fun isModified(settings: DeepBugsConfig): Boolean = false
    override fun reset(settings: DeepBugsConfig) = Unit
}
