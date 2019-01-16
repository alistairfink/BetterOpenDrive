package com.alistairfink.betteropendrive.requestModels

data class FileUploadRequest
(
        var SessionId: String,
        var FileId: String,
        var TempLocation: String,
        var ChunkOffset: Int,
        var ChunkSize: Int,
        var FileData: ByteArray
)