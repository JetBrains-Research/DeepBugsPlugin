package org.jetbrains.research.deepbugs.common

import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.ide.plugins.PluginManager
import java.io.File

object DeepBugsPlugin {
    private val classLoader: ClassLoader
        get() = this::class.java.classLoader

    val descriptor: IdeaPluginDescriptor
        get() = PluginManager.getLoadedPlugins().single { it.pluginClassLoader == classLoader }

    val name: String
        get() = descriptor.name

    val installationFolder: File
        get() = descriptor.path
}
