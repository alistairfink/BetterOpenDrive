package com.alistairfink.betteropendrive.responseModels

import com.google.gson.annotations.SerializedName

data class FolderListResponse
(
    @SerializedName("DirUpdateTime") val DirUpdateTime: Long,
    @SerializedName("Name") val Name: String,
    @SerializedName("ParentFolderID") val ParentFolderId: String,
    @SerializedName("DirectFolderLink") val DirectFolderLink: String,
    @SerializedName("ResponseType") val ResponseType: Int,
    @SerializedName("Folders") val Folders: List<Folders>,
    @SerializedName("Files") val Files: List<Files>
)

data class Folders
(
    @SerializedName("FolderID") val FolderId: String,
    @SerializedName("Name") val Name: String,
    @SerializedName("DateCreated") val DateCreated: Long,
    @SerializedName("DirUpdateTime") val DirUpdateTime: Long,
    @SerializedName("Access") val Access: Int,
    @SerializedName("DateModified") val DateModified: Long,
    @SerializedName("Shared") val Shared: Boolean,
    @SerializedName("ChildFolders") val ChildFolders: Int,
    @SerializedName("Link") val Link: String,
    @SerializedName("Encrypted") val Encrypted: Int
)

data class Files
(
    @SerializedName("FileId") val FileId: String,
    @SerializedName("Name") val Name: String,
    @SerializedName("GroupID") val GroupId: Int,
    @SerializedName("Extension") val Extension: String,
    @SerializedName("Size") val Size: Int,
    @SerializedName("Views") val Views: Int,
    @SerializedName("Version") val Version: String,
    @SerializedName("Downloads") val Downloads: Int,
    @SerializedName("DateModified") val DateModified: Long,
    @SerializedName("Access") val Access: Int,
    @SerializedName("FileHash") val FileHash: String,
    @SerializedName("Link") val Link: String,
    @SerializedName("DownloadLink") val DownloadLink: String,
    @SerializedName("StreamingLink") val StreamingLink: String,
    @SerializedName("TempStreamingLink") val TempStreamingLink: String,
    @SerializedName("ThumbLink") val ThumbLink: String,
    @SerializedName("Password") val Password: String,
    @SerializedName("EditOnline") val EditOnline: Int
)