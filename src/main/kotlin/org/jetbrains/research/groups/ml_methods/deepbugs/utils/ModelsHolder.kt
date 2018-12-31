package org.jetbrains.research.groups.ml_methods.deepbugs.utils

import com.intellij.notification.Notification
import com.intellij.notification.NotificationListener
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.application.PathManager
import com.intellij.util.io.exists
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.jetbrains.research.groups.ml_methods.deepbugs.downloader.DownloadClient
import java.nio.file.Paths
import javax.swing.event.HyperlinkEvent

object ModelsHolder {
    init {
        if (!modelFilesExists())
            showDownloadNotification()
    }

    private val root = Paths.get(PathManager.getPluginsPath(), "DeepBugsPlugin").toString()
    var nodeTypeMapping = JsonUtils.loadMapping(Paths.get(root, "models", "nodeTypeToVector.json").toString())
    var tokenMapping = JsonUtils.loadMapping(Paths.get(root, "models", "tokenToVector.json").toString())
    var typeMapping = JsonUtils.loadMapping(Paths.get(root, "models", "typeToVector.json").toString())
    var operatorMapping = JsonUtils.loadMapping(Paths.get(root, "models", "operatorToVector.json").toString())
    var binOperatorModel: MultiLayerNetwork? = null
    var binOperandModel: MultiLayerNetwork? = null
    var swappedArgsModel: MultiLayerNetwork? = null

    private fun modelFilesExists(): Boolean {
        //placeholder
        return false
    }

    private fun showDownloadNotification() {
        Notifications.Bus.notify(Notification(DeepBugsPluginBundle.message("download.notification"),
                DeepBugsPluginBundle.message("notification.title"), DeepBugsPluginBundle.message("download.notification.message"),
                NotificationType.INFORMATION, object : NotificationListener.Adapter() {
            override fun hyperlinkActivated(notification: Notification, e: HyperlinkEvent) {
                DownloadClient.downloadModels()
                notification.expire()
            }
        }).setImportant(true))
    }

    private fun checkMappings() {
        if (Paths.get(root, "models", "nodeTypeToVector.json").exists() && nodeTypeMapping == null)
            nodeTypeMapping = JsonUtils.loadMapping(Paths.get(root, "models", "nodeTypeToVector.json").toString())
        if (Paths.get(root, "models", "typeToVector.json").exists() && typeMapping == null)
            typeMapping = JsonUtils.loadMapping(Paths.get(root, "models", "typeToVector.json").toString())
        if (Paths.get(root, "models", "operatorToVector.json").exists() && operatorMapping == null)
            operatorMapping = JsonUtils.loadMapping(Paths.get(root, "models", "operatorToVector.json").toString())
        if (Paths.get(root, "models", "tokenToVector.json").exists() && tokenMapping == null)
            tokenMapping = JsonUtils.loadMapping(Paths.get(root, "models", "tokenToVector.json").toString())
    }

    private fun checkBinOperandModel() {
        if (Paths.get(root, "models", "binOperandDetectionModel.h5").exists() && binOperandModel == null) {
            binOperandModel = KerasModelImport.importKerasSequentialModelAndWeights(Paths.get(root, "models", "binOperandDetectionModel.h5").toString())
        }
    }

    private fun checkBinOperatorModel() {
        if (Paths.get(root, "models", "binOperatorDetectionModel.h5").exists() && binOperatorModel == null) {
            binOperatorModel = KerasModelImport.importKerasSequentialModelAndWeights(Paths.get(root, "models", "binOperatorDetectionModel.h5").toString())
        }
    }

    private fun checkSwappedArgsModel() {
        if (Paths.get(root, "models", "swappedArgsDetectionModel.h5").exists() && swappedArgsModel == null) {
            swappedArgsModel = KerasModelImport.importKerasSequentialModelAndWeights(Paths.get(root, "models", "swappedArgsDetectionModel.h5").toString())
        }
    }

    fun checkModels() {
        checkMappings()
        checkBinOperandModel()
        checkBinOperatorModel()
        checkSwappedArgsModel()
    }
}