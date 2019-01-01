package com.alistairfink.betteropendrive.requestModels

import com.google.gson.annotations.SerializedName

data class FolderRenameRequest
(
        @SerializedName("session_id") var SessionId: String,
        @SerializedName("folder_id") var FolderId: String,
        @SerializedName("folder_name") var NewName: String
)