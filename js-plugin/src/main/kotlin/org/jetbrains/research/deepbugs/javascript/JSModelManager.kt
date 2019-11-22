package org.jetbrains.research.deepbugs.javascript

import com.intellij.ide.plugins.PluginManager
import org.jetbrains.research.deepbugs.common.model.ModelManager
import org.jetbrains.research.deepbugs.javascript.ide.inspections.base.JSDeepBugsBaseInspection

object JSModelManager : ModelManager() {
    override val pluginName: String = PluginManager.getPluginByClassName(JSDeepBugsBaseInspection::class.java.name)!!.let {
        PluginManager.getPlugin(it)?.name!!
    }
}
