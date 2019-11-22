package org.jetbrains.research.deepbugs.common.utils

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object Json {
    const val INDENT = 4
    val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    inline fun <reified T> string(value: T): String = moshi.adapter(T::class.java).toJson(value)
    inline fun <reified T> pretty(value: T): String = moshi.adapter(T::class.java).indent(" ".repeat(INDENT)).toJson(value)
    inline fun <reified T> bytes(value: T) = moshi.adapter(T::class.java).indent(" ".repeat(INDENT)).toJson(value).byteInputStream().readBytes()

    inline fun <reified T> parse(value: String) = moshi.adapter(T::class.java).fromJson(value) as T
}
