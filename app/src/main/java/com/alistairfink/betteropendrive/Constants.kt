package com.alistairfink.betteropendrive

class Constants
{
    companion object
    {
        const val OpenDriveBaseURL = "https://dev.opendrive.com/api/v1/"
        const val KeyStoreKey = "com.alistairfink.betteropendrive.secretkey"
    }
}

class SharedPreferenceConstants
{
    companion object
    {
        const val Key = "com.alistairfink.betteropendrive.SHARED_PREFERENCES"
        const val SessionId = "OpenDriveSessionID"
        const val UserName = "OpenDriveUserName"
        const val UserNameIV = "OpenDriveUserNameIV"
        const val Password = "OpenDrivePassword"
        const val PasswordIV = "OpenDrivePasswordIV"
        const val Name = "Name"
    }
}