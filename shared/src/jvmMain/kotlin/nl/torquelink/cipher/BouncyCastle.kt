package nl.torquelink.cipher

import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object BouncyCastle {
    val cipherKey = SecretKeySpec(
        (System.getenv("CIPHER_PASS") ?: "TotallySecureCipherPass")
            .toByteArray(charset("UTF8")),
        "AES"
    )

    fun String.encrypt(): String {
        Security.addProvider(BouncyCastleProvider())
        val input = this.toByteArray(charset("UTF8"))
        synchronized(Cipher::class.java) {
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, cipherKey)

            val cipherText = ByteArray(cipher.getOutputSize(input.size))
            var ctLength = cipher.update(
                input, 0, input.size,
                cipherText, 0
            )
            ctLength += cipher.doFinal(cipherText, ctLength)
            return String(
                Base64.getEncoder().encode(cipherText)
            )
        }
    }

    fun String.decrypt(): String {
        Security.addProvider(BouncyCastleProvider())

        val input = org.bouncycastle.util.encoders.Base64
            .decode(this.trim { it <= ' ' }.toByteArray(charset("UTF8")))

        synchronized(Cipher::class.java) {
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, cipherKey)

            val plainText = ByteArray(cipher.getOutputSize(input.size))
            var ptLength = cipher.update(input, 0, input.size, plainText, 0)
            ptLength += cipher.doFinal(plainText, ptLength)
            val decryptedString = String(plainText)
            return decryptedString.trim { it <= ' ' }
        }
    }
}