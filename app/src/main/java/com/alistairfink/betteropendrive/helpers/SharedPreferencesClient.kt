package com.alistairfink.betteropendrive.helpers

import android.content.Context
import com.alistairfink.betteropendrive.SharedPreferenceConstants


class SharedPreferencesClient(context: Context) {
    private val _sharedPreferences = context.getSharedPreferences(SharedPreferenceConstants.Key, Context.MODE_PRIVATE);

    fun writeString(key: String, value: String)
    {
        with (_sharedPreferences.edit())
        {
            putString(key, value);
            apply();
        }
    }

    fun writeInt(key: String, value: Int)
    {
        with (_sharedPreferences.edit())
        {
            putInt(key, value);
            apply();
        }
    }

    fun getString(key: String) : String?
    {
        return _sharedPreferences.getString(key, null);
    }
    // TODO : Functions for retrieveing int and deleting.
}