package com.alistairfink.betteropendrive.requestModels

import com.google.gson.annotations.SerializedName

data class FileUploadCloseRequest
(
        @SerializedName("session_id") var SessionId: String,
        @SerializedName("file_id") var FileId: String,
        @SerializedName("file_size") var FileSize: Int
)