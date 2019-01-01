package com.alistairfink.betteropendrive.requestModels

import com.google.gson.annotations.SerializedName

data class FolderMoveCopyRequest
(
        @SerializedName("session_id") var SessionId: String,
        @SerializedName("folder_id") var FolderId: String,
        @SerializedName("dst_folder_id") var DestinationFolderId: String,
        @SerializedName("move") var Move: Boolean
)