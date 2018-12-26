package org.jetbrains.research.groups.ml_methods.deepbugs.utils

import com.intellij.openapi.application.PathManager
import com.intellij.util.io.exists
import org.tensorflow.SavedModelBundle
import org.tensorflow.Session
import java.nio.file.Paths

object ModelsHolder {

    private val root = Paths.get(PathManager.getPluginsPath(), "DeepBugsPlugin").toString()
    var nodeTypeMapping = DeepBugsUtils.loadMapping(Paths.get(root, "models", "nodeTypeToVector.json").toString())
    var tokenMapping = DeepBugsUtils.loadMapping(Paths.get(root, "models", "tokenToVector.json").toString())
    var typeMapping = DeepBugsUtils.loadMapping(Paths.get(root, "models", "typeToVector.json").toString())
    var operatorMapping = DeepBugsUtils.loadMapping(Paths.get(root, "models", "operatorToVector.json").toString())
    var binOperatorModelSession : Session? = null
    var binOperandModelSession : Session? = null
    var swappedArgsModelSession : Session? = null


    private fun checkMappings() {
        if (Paths.get(root, "models", "nodeTypeToVector.json").exists() && nodeTypeMapping == null)
            nodeTypeMapping = DeepBugsUtils.loadMapping(Paths.get(root, "models", "nodeTypeToVector.json").toString())
        if (Paths.get(root, "models", "typeToVector.json").exists() && typeMapping == null)
            typeMapping = DeepBugsUtils.loadMapping(Paths.get(root, "models", "typeToVector.json").toString())
        if (Paths.get(root, "models", "operatorToVector.json").exists() && operatorMapping == null)
            operatorMapping = DeepBugsUtils.loadMapping(Paths.get(root, "models", "operatorToVector.json").toString())
        if (Paths.get(root, "models", "tokenToVector.json").exists() && tokenMapping == null)
            tokenMapping = DeepBugsUtils.loadMapping(Paths.get(root, "models", "tokenToVector.json").toString())
    }

    private fun checkBinOperandModel() {
        if (Paths.get(root, "models", "binOperandDetectionModel").exists()) {
            binOperandModelSession = SavedModelBundle.load(Paths.get(root, "models", "binOperandDetectionModel").toString(), "serve").session()
        }
    }

    private fun checkBinOperatorModel() {
        if (Paths.get(root, "models", "binOperatorDetectionModel").exists()) {
            binOperatorModelSession = SavedModelBundle.load(Paths.get(root, "models", "binOperatorDetectionModel").toString(), "serve").session()
        }
    }

    private fun checkSwappedArgsModel() {
        if (Paths.get(root, "models", "swappedArgsDetectionModel").exists()) {
            swappedArgsModelSession = SavedModelBundle.load(Paths.get(root, "models", "swappedArgsDetectionModel").toString(), "serve").session()
        }
    }

    fun checkModels() {
        checkMappings()
        checkBinOperandModel()
        checkBinOperatorModel()
        checkSwappedArgsModel()
    }
}