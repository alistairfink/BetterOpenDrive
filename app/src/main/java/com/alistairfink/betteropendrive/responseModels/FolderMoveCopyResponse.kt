package com.alistairfink.betteropendrive.responseModels

import com.google.gson.annotations.SerializedName

data class FolderMoveCopyResponse
(
        @SerializedName("FolderID") val FolderID: String,
        @SerializedName("Name") val Name: String,
        @SerializedName("DateCreated") val DateCreated: Int,
        @SerializedName("DirUpdateTime") val DirUpdateTime: Int,
        @SerializedName("Access") val Access: Int,
        @SerializedName("DateModified") val DateModified: Int,
        @SerializedName("Shared") val Shared: String,
        @SerializedName("Description") val Description: String,
        @SerializedName("Link") val Link: String
)