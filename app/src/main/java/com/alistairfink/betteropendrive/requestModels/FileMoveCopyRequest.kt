package com.alistairfink.betteropendrive.requestModels

import com.google.gson.annotations.SerializedName

data class FileMoveCopyRequest
(
        @SerializedName("session_id") var SessionId: String,
        @SerializedName("src_file_id") var SourceFileId: String,
        @SerializedName("dst_folder_id") var DestinationFolderId: String,
        @SerializedName("move") var Move: String,
        @SerializedName("overwrite_if_exists") var OverWriteIfExists: String
)