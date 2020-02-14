package org.jetbrains.research.deepbugs.common.model

import org.jetbrains.research.deepbugs.common.model.common.CommonModel

object CommonModelStorage {
    val common: CommonModel by lazy { CommonModel() }
    val vocabulary: Vocabulary by lazy { Vocabulary() }
}
