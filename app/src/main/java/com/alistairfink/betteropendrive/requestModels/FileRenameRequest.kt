package com.alistairfink.betteropendrive.requestModels

import com.google.gson.annotations.SerializedName

data class FileRenameRequest
(
        @SerializedName("session_id") val SessionId: String,
        @SerializedName("new_file_name") val NewName: String,
        @SerializedName("file_id") val FileId: String
)