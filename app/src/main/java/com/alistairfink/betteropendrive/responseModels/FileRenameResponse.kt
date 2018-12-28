package com.alistairfink.betteropendrive.responseModels

import com.google.gson.annotations.SerializedName

data class FileRenameResponse
(
        @SerializedName("FileId") val FileId: String,
        @SerializedName("Name") val Name: String,
        @SerializedName("GroupID") val GroupID: Int,
        @SerializedName("Extension") val Extension: String,
        @SerializedName("Size") val Size: String,//Int,
        @SerializedName("Views") val Views: String,//Int,
        @SerializedName("Downloads") val Downloads: String,//Int,
        @SerializedName("DateModified") val DateModified: String,//Long,
        @SerializedName("Access") val Access: String,//Int,
        @SerializedName("Link") val Link: String,
        @SerializedName("DownloadLink") val DownloadLink: String,
        @SerializedName("StreamingLink") val StreamingLink: String,
        @SerializedName("TempStreamingLink") val TempStreamingLink: String,
        @SerializedName("Password") val Password: String,
        @SerializedName("Description") val Description: String,
        @SerializedName("DirUpdateTime") val DirUpdateTime: String//Long
)