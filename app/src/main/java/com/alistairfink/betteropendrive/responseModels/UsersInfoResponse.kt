package com.alistairfink.betteropendrive.responseModels

import com.google.gson.annotations.SerializedName

data class UsersInfoResponse(
        @SerializedName("UserID") val UserID: Int,
        @SerializedName("AccessUserID") val  AccessUserID: Int,
        @SerializedName("UserName") val UserName: String,
        @SerializedName("UserFirstName") val UserFirstName: String,
        @SerializedName("UserLastName") val UserLastName: String,
        @SerializedName("PrivateKey") val PrivateKey: String,
        @SerializedName("Trial") val Trial: Int,
        @SerializedName("UserSince") val UserSince: String,
        @SerializedName("BwResetLast") val BwResetLast: Int,
        @SerializedName("AccType") val AccType: Int,
        @SerializedName("MaxStorage") val  MaxStorage: Int,
        @SerializedName("StorageUsed") val StorageUsed: Int,
        @SerializedName("BwMax") val BwMax: Int,
        @SerializedName("BwUsed") val BwUsed: Int,
        @SerializedName("FVersioning") val FVersioning: Int,
        @SerializedName("FVersions") val FVersions: Int,
        @SerializedName("DailyStat") val DailyStat: Int,
        @SerializedName("UserLang") val UserLang: String,
        @SerializedName("MaxFileSize") val MaxFileSize: Int,
        @SerializedName("Level") val Level: Int,
        @SerializedName("UserPlan") val UserPlan: String,
        @SerializedName("TimeZone") val TimeZone: String,
        @SerializedName("MaxAccountUsers") val MaxAccountUsers: Int,
        @SerializedName("IsAccountUser") val IsAccountUser: Int,
        @SerializedName("CompanyName") val CompanyName: String,
        @SerializedName("Email") val Email: String,
        @SerializedName("Phone") val Phone: String,
        @SerializedName("Avatar") val Avatar: String,
        @SerializedName("AvatarColor") val AvatarColor: String,
        @SerializedName("AdminMode") val AdminMode: Int,
        @SerializedName("DueDate") val DueDate: String,
        @SerializedName("WebLink") val WebLink: String,
        @SerializedName("PublicProfiles") val PublicProfiles: Int,
        @SerializedName("RootFolderPermission") val RootFolderPermission: Int,
        @SerializedName("CanChangePwd") val CanChangePwd: Int,
        @SerializedName("IsPartner") val IsPartner: Int,
        @SerializedName("Partner") val Partner: String,
        @SerializedName("SupportUrl") val SupportUrl: String,
        @SerializedName("PartnerUsersDomain") val PartnerUsersDomain: String,
        @SerializedName("Suspended") val  Suspended: Boolean,
        @SerializedName("Affiliation") val Affiliation: String,
        @SerializedName("UserUID") val UserUID: String,
        @SerializedName("Address1") val Address1: String,
        @SerializedName("Address2") val Address2: String
)