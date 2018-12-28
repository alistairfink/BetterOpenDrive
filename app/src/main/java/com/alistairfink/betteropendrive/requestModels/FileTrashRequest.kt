package com.alistairfink.betteropendrive.requestModels

import com.google.gson.annotations.SerializedName

data class FileTrashRequest
(
        @SerializedName("session_id") var SessionId: String,
        @SerializedName("file_id") var FileId: String
)