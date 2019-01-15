package com.alistairfink.betteropendrive.helpers

import android.content.Context
import com.alistairfink.betteropendrive.SharedPreferenceConstants
import com.alistairfink.betteropendrive.apiService.repositories.OpenDriveRepositoryProvider
import com.alistairfink.betteropendrive.dataModels.FileModel
import com.alistairfink.betteropendrive.requestModels.FileMoveCopyRequest
import com.alistairfink.betteropendrive.requestModels.FileRenameRequest
import com.alistairfink.betteropendrive.requestModels.FileTrashRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.FileInputStream
import android.R.attr.process
import com.alistairfink.betteropendrive.requestModels.FileCreateRequest


class OpenDriveFileApiClient(private val context: Context)
{
    private val compositeDisposable = CompositeDisposable()

    fun download(file: FileModel, callBack: (FileModel, ByteArray) -> Unit)
    {
        var repository = OpenDriveRepositoryProvider.provideOpenDriveRepository()
        compositeDisposable.add(
                repository.downloadFile(file.FileId)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            callBack.invoke(file, result.bytes())
                        }, { error ->
                            error.printStackTrace()
                        })
        )
    }

    fun trash(fileId: String)
    {
        var sharedPreferences = SharedPreferencesClient(context)
        var sessionId = sharedPreferences.getString(SharedPreferenceConstants.SessionId)!!
        var fileTrashRequest = FileTrashRequest(
                SessionId = sessionId,
                FileId = fileId
        )
        var repository = OpenDriveRepositoryProvider.provideOpenDriveRepository()
        compositeDisposable.add(
                repository.trashFile(fileTrashRequest)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            if (result.DirUpdateTime <= 0)
                            {
                                throw Exception("This shits fucked")
                            }
                        }, { error ->
                            error.printStackTrace()
                        })
        )
        return
    }

    fun rename(newName: String, fileId: String)
    {
        var sharedPreferences = SharedPreferencesClient(context)
        var sessionId = sharedPreferences.getString(SharedPreferenceConstants.SessionId)!!
        var fileRenameRequest = FileRenameRequest(
                SessionId = sessionId,
                NewName = newName,
                FileId = fileId
        )
        var repository = OpenDriveRepositoryProvider.provideOpenDriveRepository()
        compositeDisposable.add(
                repository.fileRename(fileRenameRequest)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            if (newName != result.Name)
                            {
                                throw Exception("Sum Ting Wong")
                            }
                        }, { error ->
                            error.printStackTrace()
                        })
        )
        return
    }

    fun move(fileId: String, folderId: String)
    {
        var sharedPreferences = SharedPreferencesClient(context)
        var sessionId = sharedPreferences.getString(SharedPreferenceConstants.SessionId)!!
        var fileMoveRequest = FileMoveCopyRequest(
                SessionId = sessionId,
                SourceFileId = fileId,
                DestinationFolderId = folderId,
                Move = "true",
                OverWriteIfExists = "true"
        )
        moveCopy(fileMoveRequest)
    }

    fun copy(fileId: String, folderId: String)
    {
        var sharedPreferences = SharedPreferencesClient(context)
        var sessionId = sharedPreferences.getString(SharedPreferenceConstants.SessionId)!!
        var fileCopyRequest = FileMoveCopyRequest(
                SessionId = sessionId,
                SourceFileId = fileId,
                DestinationFolderId = folderId,
                Move = "false",
                OverWriteIfExists = "true"
        )
        moveCopy(fileCopyRequest)
    }

    fun uploadFile(uri: String, folderId: String, callback: () -> Unit)
    {
        var repository = OpenDriveRepositoryProvider.provideOpenDriveRepository()
        var sharedPreferences = SharedPreferencesClient(context)
        var sessionId = sharedPreferences.getString(SharedPreferenceConstants.SessionId)!!
        // TODO: Figure out how to check for file overwriting
        // TODO: Call to create file
        var createFileRequest = FileCreateRequest (
                SessionId = sessionId,
                FolderId = folderId,
                FileName = "", // TODO: Get this from uri
                OpenIfExists = 1
        )
        compositeDisposable.add(
                repository.fileCreate(createFileRequest)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            var chunkOffset = 0
                            var buffer = ByteArray(4096)
                            // This might not work
                            var inputStream = FileInputStream(uri)
                            var chunk: Int
                            while ({ chunk = inputStream.read(buffer); chunk }() > 0)
                            {
                                // TODO: Upload call here
                            }
                            // TODO: Call to Close file

                            inputStream.close()
                            callback.invoke()
                        }, { error ->
                            error.printStackTrace()
                        })
        )
    }

    private fun moveCopy(request: FileMoveCopyRequest)
    {
        var repository = OpenDriveRepositoryProvider.provideOpenDriveRepository()
        compositeDisposable.add(
                repository.moveCopyFile(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            if (result.FileId == request.SourceFileId)
                            {
                                throw Exception("This didn't work")
                            }
                        }, { error ->
                            error.printStackTrace()
                        })
        )
    }
}