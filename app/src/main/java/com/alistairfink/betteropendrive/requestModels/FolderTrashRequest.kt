package com.alistairfink.betteropendrive.requestModels

import com.google.gson.annotations.SerializedName

data class FolderTrashRequest
(
        @SerializedName("session_id") var SessionId: String,
        @SerializedName("folder_id") var FolderId: String
)