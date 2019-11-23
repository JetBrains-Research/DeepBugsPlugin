package org.jetbrains.research.deepbugs.common.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.cbor.Cbor

object Cbor {
    val cbor = Cbor()

    inline fun <reified T> bytes(value: T, serializer: KSerializer<T>) = cbor.dump(serializer, value)
    inline fun <reified T> parse(value: ByteArray, serializer: KSerializer<T>) = cbor.load(serializer, value)
}
