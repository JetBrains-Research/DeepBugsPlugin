package org.jetbrains.research.deepbugs.common.model

import org.jetbrains.research.deepbugs.common.DeepBugsPlugin
import org.jetbrains.research.deepbugs.common.utils.Cbor
import org.jetbrains.research.deepbugs.common.utils.Mapping
import java.io.File

object Vocabulary : AbstractStorage<Mapping>() {
    override val ext: String = "cbor"

    override fun load(name: String): Mapping = Cbor.parse(File(DeepBugsPlugin.modelsFolder, name).readBytes(), Mapping.serializer())
}
