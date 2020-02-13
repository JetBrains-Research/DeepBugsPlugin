package org.jetbrains.research.deepbugs.javascript.model

import org.jetbrains.research.deepbugs.javascript.model.specific.SpecificModel

object JSModelStorage {
    val specific by lazy { SpecificModel() }
}
