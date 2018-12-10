package com.alistairfink.betteropendrive

class Constants
{
    companion object
    {
        const val OpenDriveBaseURL = "https://dev.opendrive.com/api/v1/"
        const val KeyStoreKey = "com.alistairfink.betteropendrive.secretkey"
        // TODO : Change this iv for prod
        const val FixedIV = "0C81C77A5DDB";
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