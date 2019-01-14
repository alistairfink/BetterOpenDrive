package com.alistairfink.betteropendrive.requestModels

import com.google.gson.annotations.SerializedName

data class FileCreateRequest
(
        @SerializedName("session_id") var SessionId: String,
        @SerializedName("folder_id") var FolderId: String,
        @SerializedName("file_name") var FileName: String,
        @SerializedName("file_description") var FileDescription: String,
        @SerializedName("access_folder_id") var AccessFolderId: String,
        @SerializedName("file_size") var FileSize: Int,
        @SerializedName("file_hash") var FileHash: String,
        @SerializedName("sharing_id") var SharingId: String,
        @SerializedName("open_if_exists") var OpenIfExists: Int
)