package org.jetbrains.research.deepbugs.javascript.datatypes

import com.intellij.lang.javascript.psi.*
import com.intellij.util.ObjectUtils
import org.jetbrains.research.deepbugs.javascript.extraction.JSExtractor
import org.jetbrains.research.deepbugs.javascript.inspections.base.models
import org.jetbrains.research.deepbugs.services.datatypes.Call

class JSCall(
    callee: String,
    arguments: List<String>,
    base: String,
    argumentTypes: List<String>,
    parameters: List<String>,
    src: String
) : Call(callee, arguments, base, argumentTypes, parameters, src) {

    companion object {
        private const val SUPPORTED_ARGS_NUM = 2
        /**
         * Extract information from [JSCallExpression] and build [JSCall].
         * @param node [JSCallExpression] that should be processed.
         * @return [JSCall] with collected information.
         */
        fun collectFromJSNode(node: JSCallExpression, src: String = ""): JSCall? {
            if (node.arguments.size != SUPPORTED_ARGS_NUM)
                return null

            val callee = ObjectUtils.tryCast(node.methodExpression, JSReferenceExpression::class.java) ?: return null
            val name = JSExtractor.extractJSNodeName(callee) ?: return null

            val args = mutableListOf<String>()
            val argTypes = mutableListOf<String>()
            node.arguments.forEach { arg ->
                JSExtractor.extractJSNodeName(arg)?.let { argName -> args.add(argName) } ?: return null
                JSExtractor.extractJSNodeType(arg).let { argType -> argTypes.add(argType) }
            }

            val base = JSExtractor.extractJSNodeBase(node)

            val resolved = try {
                callee.multiResolve(false).map { it.element }.firstOrNull { it is JSFunction } as? JSFunction
            } catch (ex: Exception) {
                null
            }

            val params = resolved?.parameters?.toList()
            val paramNames = MutableList(args.size) { "" }
            paramNames.forEachIndexed { idx, _ ->
                paramNames[idx] = JSExtractor.extractJSNodeName(params?.getOrNull(idx)) ?: ""
            }
            return JSCall(name, args, base, argTypes, paramNames, src)
        }
    }

    override fun vectorize() = vectorize(
        models.modelStorage?.tokenMapping,
        models.modelStorage?.typeMapping
    )
}
