package org.jetbrains.research.groups.ml_methods.deepbugs.datatypes

import com.beust.klaxon.JsonArray
import com.google.protobuf.Internal
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceProvider
import com.intellij.util.containers.isNullOrEmpty
import com.jetbrains.python.psi.PyBinaryExpression
import com.jetbrains.python.psi.PyCallExpression
import com.jetbrains.python.psi.PyCallSiteExpression
import com.jetbrains.python.psi.PyFunction
import com.jetbrains.python.psi.impl.PyCallExpressionHelper
import com.jetbrains.python.psi.impl.PyReferenceExpressionImpl
import com.jetbrains.python.psi.resolve.PyReferenceResolveProvider
import com.jetbrains.python.psi.resolve.PyResolveContext
import org.jetbrains.research.groups.ml_methods.deepbugs.extraction.*
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.Mapping
import org.tensorflow.Tensor
import java.nio.FloatBuffer
import java.util.*

data class Call( val name: String,
                 val fstArg: String,
                 val sndArg: String,
                 val fstArgType: String,
                 val sndArgType: String,
                 val base: String,
                 var fstParam: String,
                 var sndParam: String,
                 val src: String) {

    companion object {

        /**
         * Extract information from [PyCallExpression] and build [Call].
         * @param node [PyCallExpression] that should be processed.
         * @return [Call] with collected information.
         */
        fun collectFromPyNode(node: PyCallExpression, src: String = ""): Call? {
            if (node.arguments.size != 2)
                return null
            val name = extractPyNodeName(node.callee)
                    ?: return null
            val fstArg = extractPyNodeName(node.arguments[0]) ?: return null
            val sndArg = extractPyNodeName(node.arguments[1]) ?: return null
            val fstArgType = extractPyNodeType(node.arguments[0])
            val sndArgType = extractPyNodeType(node.arguments[1])
            val base = extractPyNodeBase(node)
            val resolved = node.resolveCalleeFunction(PyResolveContext.defaultContext())
            val params = resolved?.parameterList?.parameters
            var fstParam = ""
            var sndParam = ""
            if (params != null && params.size > 1)
                if (base == "") {
                    fstParam = params[0].text.asIdentifierString()
                    sndParam = params[1].text.asIdentifierString()
                }
                else if (params.size == 3){
                    fstParam = params[1].text.asIdentifierString()
                    sndParam = params[2].text.asIdentifierString()
                }
            return Call(name, fstArg, sndArg, fstArgType, sndArgType , base, fstParam, sndParam, src)
        }
    }

    private fun vectorizeListOfArrays(arrayList: List<JsonArray<Float>>): Tensor<Float> {
        var resArray = FloatArray(0)
        for (array in arrayList) resArray += array
        return Tensor.create(longArrayOf(1, resArray.size.toLong()), FloatBuffer.wrap(resArray))
    }

    fun vectorize(token: Mapping?, type: Mapping?): Tensor<Float>? {
        val nameVector = token?.get(name) ?: return null
        val fstArgVector = token.get(fstArg) ?: return null
        val sndArgVector = token.get(sndArg) ?: return null
        val fstArgTypeVector = type?.get(fstArgType) ?: return null
        val sndArgTypeVector = type.get(sndArgType) ?: return null
        val baseVector = token.get(base) ?: JsonArray(FloatArray(200) { 0.0f }.toList())
        val fstParamVector = token.get(fstParam) ?: JsonArray(FloatArray(200) { 0.0f }.toList())
        val sndParamVector = token.get(sndParam) ?: JsonArray(FloatArray(200) { 0.0f }.toList())
        return vectorizeListOfArrays(listOf(
                nameVector, fstArgVector, sndArgVector,
                fstArgTypeVector, sndArgTypeVector,
                baseVector, fstParamVector, sndParamVector))
    }
}