package com.alistairfink.betteropendrive.helpers

import android.security.keystore.KeyProperties
import android.security.keystore.KeyGenParameterSpec
import android.util.Base64
import com.alistairfink.betteropendrive.Constants
import java.security.Key
import java.security.KeyStore
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
        fun encrypt(value: String) : String
        {
            val c = Cipher.getInstance(AES_MODE)
            c.init(Cipher.ENCRYPT_MODE, retrieveKey(), GCMParameterSpec(128, Constants.FixedIV.toByteArray(Charsets.UTF_8)))
            var test = c.iv.toString()
            val encodedBytes = c.doFinal(value.toByteArray(Charsets.UTF_8))
            return Base64.encodeToString(encodedBytes, Base64.DEFAULT)
        }
        fun decrypt(value: String) : String
        {
            val c = Cipher.getInstance(AES_MODE)
            c.init(Cipher.DECRYPT_MODE, retrieveKey(), GCMParameterSpec(128, Constants.FixedIV.toByteArray(Charsets.UTF_8)))
            return c.doFinal(value.toByteArray(Charsets.UTF_8)).toString()
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