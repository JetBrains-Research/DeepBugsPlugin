package org.jetbrains.research.deepbugs.python

import com.intellij.ide.plugins.PluginManager
import org.jetbrains.research.deepbugs.common.model.ModelManager
import org.jetbrains.research.deepbugs.python.inspections.base.PyDeepBugsBaseInspection

object PyModelManager : ModelManager() {
    override val pluginName: String = PluginManager.getPluginByClassName(PyDeepBugsBaseInspection::class.java.name)!!.let {
        PluginManager.getPlugin(it)?.name!!
    }
}
