package com.alistairfink.betteropendrive.responseModels

import com.google.gson.annotations.SerializedName

data class FileUploadCloseResponse
(
        @SerializedName("FileId") var FileId: String,
        @SerializedName("Name") var Name: String,
        @SerializedName("GroupID") var GroupId: String,
        @SerializedName("Extension") var Extension: String,
        @SerializedName("Size") var Size: String,
        @SerializedName("Views") var Views: String,
        @SerializedName("Version") var Version: String,
        @SerializedName("Downloads") var Downloads: String,
        @SerializedName("DateTrashed") var DateTrashed: String,
        @SerializedName("DateModified") var DateModified: String,
        @SerializedName("OwnerSuspended") var OwnerSuspended: Boolean,
        @SerializedName("AccType") var AccType: String,
        @SerializedName("FileHash") var FileHash: String,
        @SerializedName("Link") var Link: String,
        @SerializedName("DownloadLink") var DownloadLink: String,
        @SerializedName("StreamingLink") var StreamingLink: String,
        @SerializedName("OwnerName") var OwnerName: String,
        @SerializedName("upload_speed_limit") var UploadSpeedLimit: Int,
        @SerializedName("download_speed_limit") var DownloadSpeedLimit: Int,
        @SerializedName("BWExceeded") var BWExceeded: Int,
        @SerializedName("ThumbLink") var ThumbLink: String,
        @SerializedName("Encrypted") var Encrypted: String,
        @SerializedName("Password") var Password: String,
        @SerializedName("OwnerLevel") var OwnerLevel: String,
        @SerializedName("EditOnline") var EditOnline: Int,
        @SerializedName("ID") var Id: String,
        @SerializedName("FolderID") var FolderId: String,
        @SerializedName("Description") var Description: String,
        @SerializedName("IsArchive") var IsArchive: String,
        @SerializedName("Category") var Category: String,
        @SerializedName("Date") var Date: String,
        @SerializedName("DateUploaded") var DateUploaded: Int,
        @SerializedName("DateAccessed") var DateAccessed: String,
        @SerializedName("DirectLinkPublic") var DirectLinkPublic: String,
        @SerializedName("EmbedLink") var EmbedLink: String,
        @SerializedName("AccessDisabled") var AccessDisabled: Int,
        @SerializedName("Type") var Type: String,
        @SerializedName("DestURL") var DestUrl: String,
        @SerializedName("Owner") var Owner: String,
        @SerializedName("AccessUser") var AccessUser: String,
        @SerializedName("DirUpdateTime") var DirUpdateTime: Int,
        @SerializedName("FileName") var FileName: String,
        @SerializedName("FileDate") var FileDate: String,
        @SerializedName("FileDescription") var FileDescription: String,
        @SerializedName("FileDestUrl") var FileDestUrl: String,
        @SerializedName("FileKey") var FileKey: String,
        @SerializedName("FilePrice") var FilePrice: String,
        @SerializedName("FileVersion") var FileVersion: String,
        @SerializedName("FileIp") var FileIp: String,
        @SerializedName("FileIsPublic") var FileIsPublic: String,
        @SerializedName("Datetime") var Datetime: String
)