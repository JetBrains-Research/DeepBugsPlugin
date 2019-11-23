package org.jetbrains.research.deepbugs.common.utils.errors.github

import com.intellij.util.Base64
import java.io.InputStream
import java.io.ObjectInputStream
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Provides functionality to decode secret tokens to make them not directly readable
 */
object GitHubTokenScrambler {
    //FIXME-review Is it actually random?))
    private const val myInitVector = "RandomInitVector"
    private const val myKey = "GitHubErrorToken"

    internal fun decrypt(inputStream: InputStream): String {
        val iv = IvParameterSpec(myInitVector.toByteArray(charset("UTF-8")))
        val keySpec = SecretKeySpec(myKey.toByteArray(charset("UTF-8")), "AES")

        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.DECRYPT_MODE, keySpec, iv)

        val original = cipher.doFinal(Base64.decode(ObjectInputStream(inputStream).readObject() as String))
        return String(original)
    }
}
