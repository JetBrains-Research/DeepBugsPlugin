package org.jetbrains.research.deepbugs.services.errors

import com.intellij.util.Base64
import java.io.FileOutputStream
import java.io.InputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Provides functionality to encode and decode secret tokens to make them not directly readable. Let me be clear:
 * THIS IS THE OPPOSITE OF SECURITY!
 */
object GitHubAccessTokenScrambler {
    private const val myInitVector = "RandomInitVector"
    private const val myKey = "GitHubErrorToken"

    @JvmStatic
    fun main(args: Array<String>) {
        if (args.size != 2) {
            return
        }
        val horse = args[0]
        val outputFile = args[1]
        try {
            val e = encrypt(horse)
            val o = ObjectOutputStream(FileOutputStream(outputFile))
            o.writeObject(e)
            o.close()
        } catch (e: Exception) {
            e.printStackTrace()
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
            ex.printStackTrace()
        }

        return null
    }

    @Throws(Exception::class)
    internal fun decrypt(inputStream: InputStream): String {
        val `in`: String
        val o = ObjectInputStream(inputStream)
        `in` = o.readObject() as String
        val iv = IvParameterSpec(myInitVector.toByteArray(charset("UTF-8")))
        val keySpec = SecretKeySpec(myKey.toByteArray(charset("UTF-8")), "AES")

        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.DECRYPT_MODE, keySpec, iv)

        val original = cipher.doFinal(Base64.decode(`in`))
        return String(original)
    }
}
