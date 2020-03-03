package org.jetbrains.research.deepbugs.common

import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.ide.plugins.PluginManager
import com.intellij.internal.statistic.utils.PluginInfo
import com.intellij.internal.statistic.utils.getPluginInfoById
import com.intellij.openapi.application.ApplicationManager
import org.jetbrains.annotations.TestOnly
import java.io.File

object DeepBugsPlugin {
    private val classLoader: ClassLoader
        get() = this::class.java.classLoader

    private var myTestPluginId: String? = null

    val descriptor: IdeaPluginDescriptor
        get() = PluginManager.getLoadedPlugins().single {
            (ApplicationManager.getApplication().isUnitTestMode && it.pluginId.idString == myTestPluginId) ||
                (ApplicationManager.getApplication().isUnitTestMode.not() && it.pluginClassLoader == classLoader)
        }

    val name: String
        get() = descriptor.name

    val installationFolder: File
        get() = descriptor.path

    val pluginId: String
        get() = descriptor.pluginId.idString

    val info: PluginInfo
        get() = getPluginInfoById(descriptor.pluginId)

    @TestOnly
    fun setTestPlugin(id: String) {
        myTestPluginId = id
    }
}
