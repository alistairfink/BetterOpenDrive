package com.alistairfink.betteropendrive.requestModels

import com.google.gson.annotations.SerializedName

data class FileRenameRequest
(
        @SerializedName("session_id") var SessionId: String,
        @SerializedName("new_file_name") var NewName: String,
        @SerializedName("file_id") var FileId: String
)