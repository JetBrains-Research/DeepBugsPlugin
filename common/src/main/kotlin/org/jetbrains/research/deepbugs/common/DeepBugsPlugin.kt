package org.jetbrains.research.deepbugs.common

import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.ide.plugins.PluginManager
import com.intellij.openapi.extensions.PluginId
import org.jetbrains.annotations.TestOnly
import com.intellij.openapi.application.ApplicationManager
import java.io.File

object DeepBugsPlugin {
    private val classLoader: ClassLoader
        get() = this::class.java.classLoader

    private var myTestPlugin: String? = null

    val descriptor: IdeaPluginDescriptor
        get() = PluginManager.getLoadedPlugins().single {
            (ApplicationManager.getApplication().isUnitTestMode && it.name == myTestPlugin) ||
                (ApplicationManager.getApplication().isUnitTestMode.not() && it.pluginClassLoader == classLoader)
        }

    val name: String
        get() = descriptor.name

    val installationFolder: File
        get() = descriptor.path

    val dependentPlugins: Array<PluginId>
        get() = descriptor.dependentPluginIds

    @TestOnly
    fun setTestPlugin(plugin: String) {
        myTestPlugin = plugin
    }
}
