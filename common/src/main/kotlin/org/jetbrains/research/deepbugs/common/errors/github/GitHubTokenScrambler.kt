package org.jetbrains.research.deepbugs.common.errors.github

import com.intellij.util.Base64
import java.io.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Provides functionality to encode and decode secret tokens to make them not directly readable. Let me be clear:
 * THIS IS THE OPPOSITE OF SECURITY!
 */
object GitHubTokenScrambler {
    //FIXME-review Is it actually random?))
    private const val myInitVector = "RandomInitVector"
    private const val myKey = "GitHubErrorToken"

    fun main(args: Array<String>) {
        if (args.size != 2) return
        //FIXME-review What is horse?
        val horse = args[0]
        val outputFile = args[1]

        ObjectOutputStream(File(outputFile).outputStream()).use {
            it.writeObject(encrypt(horse))
        }
    }

    private fun encrypt(value: String): String? {
        try {
            val iv = IvParameterSpec(myInitVector.toByteArray(charset("UTF-8")))
            val keySpec = SecretKeySpec(myKey.toByteArray(charset("UTF-8")), "AES")

            val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv)

            val encrypted = cipher.doFinal(value.toByteArray())
            return Base64.encode(encrypted)
        } catch (ex: Exception) {
            //FIXME-review Here and everywhere use loggers
            ex.printStackTrace()
        }

        return null
    }

    internal fun decrypt(inputStream: InputStream): String {
        val iv = IvParameterSpec(myInitVector.toByteArray(charset("UTF-8")))
        val keySpec = SecretKeySpec(myKey.toByteArray(charset("UTF-8")), "AES")

        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.DECRYPT_MODE, keySpec, iv)

        val original = cipher.doFinal(Base64.decode(ObjectInputStream(inputStream).readObject() as String))
        return String(original)
    }
}
