package org.jetbrains.research.groups.ml_methods.deepbugs.datatypes

import com.beust.klaxon.JsonArray
import com.jetbrains.python.psi.PyCallExpression
import com.jetbrains.python.psi.resolve.PyResolveContext
import org.jetbrains.research.groups.ml_methods.deepbugs.extraction.Extractor
import org.jetbrains.research.groups.ml_methods.deepbugs.extraction.asIdentifierString
import org.jetbrains.research.groups.ml_methods.deepbugs.inspections.utils.InspectionUtils
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.Mapping
import org.nd4j.linalg.api.ndarray.INDArray

data class Call(val name: String,
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
            val name = Extractor.extractPyNodeName(node.callee) ?: return null
            val fstArg = Extractor.extractPyNodeName(node.arguments[0]) ?: return null
            val sndArg = Extractor.extractPyNodeName(node.arguments[1]) ?: return null
            val fstArgType = Extractor.extractPyNodeType(node.arguments[0])
            val sndArgType = Extractor.extractPyNodeType(node.arguments[1])
            val base = Extractor.extractPyNodeBase(node)
            val resolved = node.multiResolveCallee(PyResolveContext.defaultContext()).firstOrNull()?.element
            val params = resolved?.parameterList?.parameters
            var fstParam = ""
            var sndParam = ""
            if (params != null && params.size > 1)
                if (base == "") {
                    fstParam = params[0].text.asIdentifierString()
                    sndParam = params[1].text.asIdentifierString()
                } else if (params.size == 3) {
                    fstParam = params[1].text.asIdentifierString()
                    sndParam = params[2].text.asIdentifierString()
                }
            return Call(name, fstArg, sndArg, fstArgType, sndArgType, base, fstParam, sndParam, src)
        }
    }

    fun vectorize(token: Mapping?, type: Mapping?): INDArray? {
        val nameVector = token?.get(name) ?: return null
        val fstArgVector = token.get(fstArg) ?: return null
        val sndArgVector = token.get(sndArg) ?: return null
        val fstArgTypeVector = type?.get(fstArgType) ?: return null
        val sndArgTypeVector = type.get(sndArgType) ?: return null
        val baseVector = token.get(base) ?: JsonArray(DoubleArray(200) { 0.0 }.toList())
        val fstParamVector = token.get(fstParam) ?: JsonArray(DoubleArray(200) { 0.0 }.toList())
        val sndParamVector = token.get(sndParam) ?: JsonArray(DoubleArray(200) { 0.0 }.toList())
        return InspectionUtils.vectorizeListOfArrays(listOf(
                nameVector, fstArgVector, sndArgVector,
                fstArgTypeVector, sndArgTypeVector,
                baseVector, fstParamVector, sndParamVector))
    }
}