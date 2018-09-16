package com.jetbrains.bogomolov

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiManager
import com.jetbrains.python.psi.PyFile
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport
import java.io.File

fun loadModel(path: String) {
    val model = KerasModelImport.importKerasSequentialModelAndWeights(
            "$path/models/binOpsDetectionModel.h5")
    println(model.layerNames)
    println(model.labels)
    model.printConfiguration()
}

fun loadJson(path: String) {
    val parser = Parser()
    val result = parser.parse("$path/models/node_type_to_vector_py.json") as JsonObject
//    val types = parse() as JsonObject
    val arr = result.array<Double>("Assert")
    println(arr!!)
    println(result.string("something"))
}

fun loadPsiFile(path: String) {
    println(ProjectManager.getInstance().openProjects.size)
    val file = LocalFileSystem.getInstance().findFileByIoFile(File(path)) ?: return
    val psiFile =
            PsiManager.getInstance(ProjectManager.getInstance().defaultProject).findFile(file) ?: return
    if (psiFile is PyFile) {
        println("We did it!")
    }
}

fun main(args: Array<String>) {
    val path = System.getProperty("user.dir")
//    loadModel(path)
//    loadJson(path)
    loadPsiFile("$path/data/SampleFile.py")
}