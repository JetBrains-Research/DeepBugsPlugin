package org.jetbrains.research.groups.ml_methods.deepbugs

import com.intellij.codeInspection.InspectionToolProvider
import org.jetbrains.research.groups.ml_methods.deepbugs.downloader.DownloadHelper
import org.jetbrains.research.groups.ml_methods.deepbugs.inspections.DeepBugsBinOperandInspection
import org.jetbrains.research.groups.ml_methods.deepbugs.inspections.DeepBugsBinOperatorInspection
import org.jetbrains.research.groups.ml_methods.deepbugs.inspections.DeepBugsSwappedArgsInspection

class DeepBugsProvider : InspectionToolProvider {

    init {
        DownloadHelper.downloadModelsAndEmbeddings()
    }
    override fun getInspectionClasses(): Array<Class<*>> {
        return arrayOf(DeepBugsBinOperatorInspection::class.java, DeepBugsBinOperandInspection::class.java,
                DeepBugsSwappedArgsInspection::class.java)
    }
}