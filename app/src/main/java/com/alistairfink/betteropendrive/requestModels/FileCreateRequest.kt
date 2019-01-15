package com.alistairfink.betteropendrive.requestModels

import com.google.gson.annotations.SerializedName

data class FileCreateRequest
(
        @SerializedName("session_id") var SessionId: String,
        @SerializedName("folder_id") var FolderId: String,
        @SerializedName("file_name") var FileName: String,
        @SerializedName("open_if_exists") var OpenIfExists: Int
)