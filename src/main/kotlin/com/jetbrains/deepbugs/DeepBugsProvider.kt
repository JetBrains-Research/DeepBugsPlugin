package com.jetbrains.deepbugs

import com.intellij.codeInspection.InspectionToolProvider

class DeepBugsProvider : InspectionToolProvider {
    override fun getInspectionClasses(): Array<Class<*>> {
        return arrayOf(DeepBugsInspection::class.java)
    }
}