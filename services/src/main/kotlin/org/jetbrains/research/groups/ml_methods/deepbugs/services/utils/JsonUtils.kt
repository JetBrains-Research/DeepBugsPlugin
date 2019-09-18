package org.jetbrains.research.groups.ml_methods.deepbugs.services.utils

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule

import java.util.*

import kotlin.reflect.KClass

object JsonUtils {

    private val jsonMapper = ObjectMapper().apply {
        registerModule(KotlinModule())
        setTimeZone(TimeZone.getDefault())
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    }

    fun <T : Any> readValue(serialized: String, klass: KClass<T>): T {
        return jsonMapper.readValue(serialized, klass.java)
    }

    fun writeValueAsString(obj: Any): String {
        return jsonMapper.writeValueAsString(obj)
    }

    fun <E : Collection<T>, T : Any> readCollectionValue(serialized: String, collection: KClass<E>, klass: KClass<T>): E {
        val type = TypeFactory.defaultInstance()
        return jsonMapper.readValue(serialized, type.constructCollectionType(collection.java, klass.java))
    }
}