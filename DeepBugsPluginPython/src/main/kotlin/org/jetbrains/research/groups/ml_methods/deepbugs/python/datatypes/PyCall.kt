package org.jetbrains.research.groups.ml_methods.deepbugs.python.datatypes

import com.jetbrains.python.psi.PyCallExpression
import com.jetbrains.python.psi.resolve.PyResolveContext
import org.jetbrains.research.groups.ml_methods.deepbugs.python.extraction.Extractor
import org.jetbrains.research.groups.ml_methods.deepbugs.services.models_manager.ModelsManager
import org.jetbrains.research.groups.ml_methods.deepbugs.services.datatypes.Call

class PyCall(callee: String,
             arguments: List<String>,
             argumentTypes: List<String>,
             base: String,
             parameters: List<String>,
             src: String) : Call(callee, arguments, argumentTypes, base, parameters, src) {

    companion object {

        /**
         * Extract information from [PyCallExpression] and build [PyCall].
         * @param node [PyCallExpression] that should be processed.
         * @return [PyCall] with collected information.
         */
        fun collectFromPyNode(node: PyCallExpression, src: String = ""): PyCall? {

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
            return PyCall(name, args, argTypes, base, paramNames, src)
        }
    }

    override fun vectorize() = vectorize(ModelsManager.tokenMapping, ModelsManager.typeMapping)
}

fun PyCallExpression.collectFromPyNode() = PyCall.collectFromPyNode(this)