package org.jetbrains.research.groups.ml_methods.deepbugs.services.logging.events

import org.jetbrains.research.groups.ml_methods.deepbugs.services.datatypes.NodeType

data class BugReport(
        val exprType: String,
        val inspection: String,
        val probability: Float
): LogData {
    constructor(
            nodeType: NodeType,
            inspection: String,
            probability: Float
    ) : this(nodeType.nodeName, inspection, probability)
}