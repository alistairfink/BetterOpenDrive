package com.alistairfink.betteropendrive.helpers

import android.content.Context
import com.alistairfink.betteropendrive.SharedPreferenceConstants


class SharedPreferencesClient(context: Context) {
    private val _sharedPreferences = context.getSharedPreferences(SharedPreferenceConstants.Key, Context.MODE_PRIVATE)

    fun writeString(key: String, value: String)
    {
        with (_sharedPreferences.edit())
        {
            putString(key, value)
            apply()
        }
    }

    fun writeInt(key: String, value: Int)
    {
        with (_sharedPreferences.edit())
        {
            putInt(key, value)
            apply()
        }
    }

    fun writeEncryptedCredentials(encryptedCredentials: EncryptedCredentials)
    {
        with(_sharedPreferences.edit())
        {
            putString(SharedPreferenceConstants.UserName, encryptedCredentials.UserName)
            putString(SharedPreferenceConstants.UserNameIV, encryptedCredentials.UserNameIV)
            putString(SharedPreferenceConstants.Password, encryptedCredentials.Password)
            putString(SharedPreferenceConstants.PasswordIV, encryptedCredentials.PasswordIV)
            apply()
        }
    }

    fun getEncryptedCredentials() : EncryptedCredentials
    {
        return EncryptedCredentials(
                UserName = _sharedPreferences.getString(SharedPreferenceConstants.UserName, null),
                UserNameIV = _sharedPreferences.getString(SharedPreferenceConstants.UserNameIV, null),
                Password = _sharedPreferences.getString(SharedPreferenceConstants.Password, null),
                PasswordIV = _sharedPreferences.getString(SharedPreferenceConstants.PasswordIV, null)
        )
    }

    fun getString(key: String) : String?
    {
        return _sharedPreferences.getString(key, null);
    }

    fun removeKey(key: String)
    {
        with(_sharedPreferences.edit())
        {
            remove(key)
            apply()
        }
    }
    // TODO : Functions for retrieveing int and deleting.
}

data class EncryptedCredentials(
        val UserName: String?,
        val UserNameIV: String?,
        val Password: String?,
        val PasswordIV: String?
)