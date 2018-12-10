package com.alistairfink.betteropendrive

class Constants
{
    companion object
    {
        const val OpenDriveBaseURL = "https://dev.opendrive.com/api/v1/"
        const val KeyStoreKey = "com.alistairfink.betteropendrive.secretkey"
        const val FixedIV = "0C81C77A5DDB678EE16FA2D5";
    }
}

class SharedPreferenceConstants
{
    companion object
    {
        const val Key = "com.alistairfink.betteropendrive.SHARED_PREFERENCES"
        const val SessionId = "OpenDriveSessionID"
        const val UserName = "OpenDriveUserName"
        const val Password = "OpenDrivePassword"
    }
}