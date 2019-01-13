package com.alistairfink.betteropendrive.requestModels

import com.google.gson.annotations.SerializedName

data class FolderCreateRequest
(
        @SerializedName("session_id") var SessionId: String,
        @SerializedName("folder_name") var FolderName: String,
        @SerializedName("folder_sub_parent") var FolderSubParentId: String
)