package com.alistairfink.betteropendrive.helpers

import android.security.keystore.KeyProperties
import android.security.keystore.KeyGenParameterSpec
import android.util.Base64
import com.alistairfink.betteropendrive.Constants
import java.security.Key
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.GCMParameterSpec


class EncryptionHelper
{
    companion object {
        fun generateSecretKey()
        {
            var keyStore = KeyStore.getInstance(AndroidKeyStore)
            keyStore.load(null)
            if (!keyStore.containsAlias(Constants.KeyStoreKey)) {
                val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, AndroidKeyStore)
                keyGenerator.init(
                        KeyGenParameterSpec.Builder(Constants.KeyStoreKey,
                                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                                .setBlockModes(KeyProperties.BLOCK_MODE_GCM).setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                                .setRandomizedEncryptionRequired(false)
                                .build())
                keyGenerator.generateKey()
            }
        }
        fun encrypt(value: String) : EncryptionData
        {
            val random = SecureRandom()
            val randomIV = ByteArray(12)
            random.nextBytes(randomIV)
            val c = Cipher.getInstance(AES_MODE)
            c.init(Cipher.ENCRYPT_MODE, retrieveKey(), GCMParameterSpec(128, randomIV))
            val encodedBytes = c.doFinal(value.toByteArray(Charsets.UTF_8))
            return EncryptionData(
                    Value = Base64.encodeToString(encodedBytes, Base64.DEFAULT),
                    IV = Base64.encodeToString(randomIV, Base64.DEFAULT)
                    )
        }
        fun decrypt(encryptedData: EncryptionData) : String
        {
            var value = Base64.decode(encryptedData.Value, Base64.DEFAULT)
            var iv = Base64.decode(encryptedData.IV, Base64.DEFAULT)
            val c = Cipher.getInstance(AES_MODE)
            c.init(Cipher.DECRYPT_MODE, retrieveKey(), GCMParameterSpec(128, iv))
            return String(c.doFinal(value), Charsets.UTF_8)
        }
    }
}

private val AndroidKeyStore = "AndroidKeyStore"
private val AES_MODE = "AES/GCM/NoPadding"
private fun retrieveKey() : Key
{
    var keyStore = KeyStore.getInstance(AndroidKeyStore)
    keyStore.load(null)
    return keyStore.getKey(Constants.KeyStoreKey, null)
}

data class EncryptionData(
        val Value: String,
        val IV: String
)