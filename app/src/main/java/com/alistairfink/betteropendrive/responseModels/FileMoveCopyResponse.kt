package com.alistairfink.betteropendrive.responseModels

import com.google.gson.annotations.SerializedName

data class FileMoveCopyResponse
(
        @SerializedName("FileId") val FileId: String,
        @SerializedName("Name") val Name: String,
        @SerializedName("GroupID") val GroupId: Int,
        @SerializedName("Extension") val Extension: String,
        @SerializedName("Size") val Size: Int,
        @SerializedName("Views") val Views: Int,
        @SerializedName("Downloads") val Downloads: Int,
        @SerializedName("DateModified") val DateModified: Long,
        @SerializedName("Access") val Access: Int,
        @SerializedName("Link") val Link: String,
        @SerializedName("DownloadLink") val DownloadLink: String,
        @SerializedName("StreamingLink") val StreamingLink: String,
        @SerializedName("TempStreamingLink") val TempStreamingLink: String,
        @SerializedName("Password") val Password: String,
        @SerializedName("Description") val Description: String,
        @SerializedName("DirUpdateTimeSrc") val DirUpdateTimeSrc: Long,
        @SerializedName("DirUpdateTime") val DirUpdateTime: Long
)