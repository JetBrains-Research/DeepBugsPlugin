package org.jetbrains.research.deepbugs.common.utils.platform

import com.intellij.ide.ApplicationInitializedListener

class PlatformInitializer : ApplicationInitializedListener {
    override fun componentsInitialized() {
        PlatformManager.checkPlatformAndDependencies()
    }
}
