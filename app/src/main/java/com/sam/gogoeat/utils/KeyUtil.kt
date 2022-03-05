package com.sam.gogoeat.utils

import android.util.Base64.*
import java.io.UnsupportedEncodingException
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.*
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


object KeyUtil {

    private const val GOGO_EAT_KEY_PASS = "gogoeatsam8481"

    /**
     * Encodes a String in AES-256 with a given key
     *
     * @param context
     * @param password
     * @param text
     * @return String Base64 and AES encoded String
     */
    fun encodeKey(stringToEncode: String, keyString: String = GOGO_EAT_KEY_PASS): String {
        if (keyString.isEmpty()) {
            throw NullPointerException("Please give Password")
        }
        if (stringToEncode.isEmpty()) {
            throw NullPointerException("Please give text")
        }
        try {
            val skeySpec: SecretKeySpec = getKey(keyString)
            val clearText = stringToEncode.toByteArray(charset("UTF8"))

            // IMPORTANT TO GET SAME RESULTS ON iOS and ANDROID
            val iv = ByteArray(16)
            Arrays.fill(iv, 0x00.toByte())
            val ivParameterSpec = IvParameterSpec(iv)

            // Cipher is not thread safe
            val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec)
            val encrypedValue: String =
                encodeToString(cipher.doFinal(clearText), DEFAULT)
            Logger.d("Encrypted: $stringToEncode -> $encrypedValue")
            return encrypedValue
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        } catch (e: InvalidAlgorithmParameterException) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * Decodes a String using AES-256 and Base64
     *
     * @param context
     * @param password
     * @param text
     * @return desoded String
     */
    fun decodeKey(text: String, password: String = GOGO_EAT_KEY_PASS): String {
        if (password.isEmpty()) {
            throw NullPointerException("Please give Password")
        }
        if (text.isEmpty()) {
            throw NullPointerException("Please give text")
        }
        try {
            val key: SecretKey = getKey(password)

            // IMPORTANT TO GET SAME RESULTS ON iOS and ANDROID
            val iv = ByteArray(16)
            Arrays.fill(iv, 0x00.toByte())
            val ivParameterSpec = IvParameterSpec(iv)
            val encrypedPwdBytes: ByteArray = decode(text, DEFAULT)
            // cipher is not thread safe
            val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
            cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec)
            val decrypedValueBytes: ByteArray = cipher.doFinal(encrypedPwdBytes)
            val decrypedValue = String(decrypedValueBytes)
            Logger.d("Decrypted: $text -> $decrypedValue")
            return decrypedValue
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        } catch (e: InvalidAlgorithmParameterException) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * Generates a SecretKeySpec for given password
     *
     * @param password
     * @return SecretKeySpec
     * @throws UnsupportedEncodingException
     */
    @Throws(UnsupportedEncodingException::class)
    private fun getKey(password: String): SecretKeySpec {

        // You can change it to 128 if you wish
        val keyLength = 256
        val keyBytes = ByteArray(keyLength / 8)
        // explicitly fill with zeros
        Arrays.fill(keyBytes, 0x0.toByte())

        // if password is shorter then key length, it will be zero-padded
        // to key length
        val passwordBytes = password.toByteArray(charset("UTF-8"))
        val length =
            if (passwordBytes.size < keyBytes.size) passwordBytes.size else keyBytes.size
        System.arraycopy(passwordBytes, 0, keyBytes, 0, length)
        return SecretKeySpec(keyBytes, "AES")
    }
}