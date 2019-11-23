package org.jetbrains.research.deepbugs.python.datatypes

import com.jetbrains.python.psi.PyCallExpression
import com.jetbrains.python.psi.resolve.PyResolveContext
import org.jetbrains.research.deepbugs.python.extraction.PyExtractor
import org.jetbrains.research.deepbugs.common.datatypes.Call
import org.jetbrains.research.deepbugs.common.model.ModelManager

class PyCall(
    callee: String,
    arguments: List<String>,
    base: String,
    argumentTypes: List<String>,
    parameters: MutableList<String>,
    src: String
) : Call(callee, arguments, base, argumentTypes, parameters, src) {

    companion object {
        private const val SUPPORTED_ARGS_NUM = 2
        /**
         * Extract information from [PyCallExpression] and build [PyCall].
         * @param node [PyCallExpression] that should be processed.
         * @return [PyCall] with collected information.
         */
        fun collectFromPyNode(node: PyCallExpression, src: String = ""): PyCall? {
            if (node.arguments.size != SUPPORTED_ARGS_NUM)
                return null

            val name = PyExtractor.extractPyNodeName(node.callee) ?: return null

            val args = mutableListOf<String>()
            val argTypes = mutableListOf<String>()
            node.arguments.forEach { arg ->
                PyExtractor.extractPyNodeName(arg)?.let { argName -> args.add(argName) } ?: return null
                PyExtractor.extractPyNodeType(arg).let { argType -> argTypes.add(argType) }
            }

            val base = PyExtractor.extractPyNodeBase(node)

            val resolved = node.multiResolveCalleeFunction(PyResolveContext.defaultContext()).firstOrNull()
            var params = resolved?.parameterList?.parameters?.toList()
            if (!params.isNullOrEmpty() && params.first().isSelf && params.size > args.size)
                params = params.drop(1)
            val paramNames = MutableList(args.size) { "" }
            paramNames.forEachIndexed { idx, _ ->
                paramNames[idx] = PyExtractor.extractPyNodeName(params?.getOrNull(idx)) ?: ""
            }
            return PyCall(name, args, base, argTypes, paramNames, src)
        }
    }

    override fun vectorize() = ModelManager.storage?.let { storage ->
        vectorize(storage.tokenMapping, storage.typeMapping)
    }
}
