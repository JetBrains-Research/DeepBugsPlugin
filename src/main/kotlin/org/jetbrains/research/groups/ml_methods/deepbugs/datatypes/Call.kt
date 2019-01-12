package org.jetbrains.research.groups.ml_methods.deepbugs.datatypes

import com.jetbrains.python.psi.PyCallExpression
import com.jetbrains.python.psi.resolve.PyResolveContext
import org.jetbrains.research.groups.ml_methods.deepbugs.extraction.Extractor
import org.jetbrains.research.groups.ml_methods.deepbugs.inspections.utils.InspectionUtils
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.Mapping
import org.tensorflow.Tensor

data class Call(val callee: String,
                val arguments: List<String>,
                val argumentTypes: List<String>,
                val base: String,
                val parameters: List<String>,
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
            val args = mutableListOf<String>()
            val argTypes = mutableListOf<String>()
            node.arguments.forEach { arg ->
                Extractor.extractPyNodeName(arg)?.let { argName -> args.add(argName) } ?: return null
                Extractor.extractPyNodeType(arg).let { argType -> argTypes.add(argType) }
            }
            val base = Extractor.extractPyNodeBase(node)
            val resolved = node.multiResolveCalleeFunction(PyResolveContext.defaultContext()).firstOrNull()
            var params = resolved?.parameterList?.parameters?.toList()
            if (!params.isNullOrEmpty() && params.first().isSelf && params.size > args.size)
                params = params.drop(1)
            val paramNames = MutableList(args.size) { "" }
            paramNames.forEachIndexed { idx, _ ->
                paramNames[idx] = Extractor.extractPyNodeName(params?.getOrNull(idx)) ?: ""
            }
            return Call(name, args, argTypes, base, paramNames, src)
        }
    }

    fun vectorize(token: Mapping?, type: Mapping?): Tensor<Float>? {
        val nameVector = token?.get(callee) ?: return null
        val argVectors = arguments.map { arg -> token.get(arg) ?: return null }
                .reduce { acc, arg -> acc + arg }
        val typeVectors = argumentTypes.map { argType -> type?.get(argType) ?: return null }
                .reduce { acc, argType -> acc + argType }
        val paramVectors = parameters.map { param -> token.get(param) ?: FloatArray(200) { 0.0f } }
                .reduce { acc, param -> acc + param }
        val baseVector = token.get(base) ?: FloatArray(200) { 0.0f }
        return InspectionUtils.vectorizeListOfArrays(listOf(nameVector, argVectors, typeVectors,
                baseVector, paramVectors))
    }
}