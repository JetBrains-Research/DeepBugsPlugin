package org.jetbrains.research.deepbugs.common.utils

import com.intellij.util.io.readText
import java.nio.file.Path

//FIXME-review Should be replaces with some binary format. Since we compress Lists of floats you look at fpzip,
//zfp or fpc-compression (https://github.com/kutschkem/fpc-compression)
class Mapping(jsonFile: Path) {
    private val json: Map<String, List<Float>> by lazy { Json.parse<Map<String, List<Float>>>(jsonFile.readText()) }

    fun get(fieldName: String): List<Float>? = json[fieldName]
}
