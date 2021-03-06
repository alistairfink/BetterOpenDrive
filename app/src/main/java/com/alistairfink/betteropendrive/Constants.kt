package com.alistairfink.betteropendrive

class Constants
{
    companion object
    {
        const val OpenDriveBaseURL = "https://dev.opendrive.com/api/v1/"
        const val KeyStoreKey = "com.alistairfink.betteropendrive.secretkey"
        const val FolderIDPrefix = "folder_"
        const val FileIDPrefix = "file_"
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
        const val Avatar = "Avatar"
    }
}

class InternalStroageConstants
{
    companion object
    {
        const val FolderPrefix = "folder_"
    }
}