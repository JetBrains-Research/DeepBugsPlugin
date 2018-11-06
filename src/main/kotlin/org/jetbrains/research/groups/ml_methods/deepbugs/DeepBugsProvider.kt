package org.jetbrains.research.groups.ml_methods.deepbugs

import com.intellij.codeInspection.InspectionToolProvider
import org.jetbrains.research.groups.ml_methods.deepbugs.downloader.DownloadHelper
import org.jetbrains.research.groups.ml_methods.deepbugs.inspections.DeepBugsBinOperandInspection
import org.jetbrains.research.groups.ml_methods.deepbugs.inspections.DeepBugsBinOperatorInspection

class DeepBugsProvider : InspectionToolProvider {

    override fun getInspectionClasses(): Array<Class<*>> {
        if (DownloadHelper.downloadModelsAndEmbeddings())
            return arrayOf(DeepBugsBinOperatorInspection::class.java, DeepBugsBinOperandInspection::class.java)
        return arrayOf()
    }
}