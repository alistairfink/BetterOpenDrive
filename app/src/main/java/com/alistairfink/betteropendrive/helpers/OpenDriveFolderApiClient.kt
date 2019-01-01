package com.alistairfink.betteropendrive.helpers

import android.content.Context
import com.alistairfink.betteropendrive.InternalStroageConstants
import com.alistairfink.betteropendrive.SharedPreferenceConstants
import com.alistairfink.betteropendrive.apiService.repositories.OpenDriveRepositoryProvider
import com.alistairfink.betteropendrive.dataModels.FolderModel
import com.alistairfink.betteropendrive.dataModels.FolderModelHelper
import com.alistairfink.betteropendrive.requestModels.FolderMoveCopyRequest
import com.alistairfink.betteropendrive.requestModels.FolderRenameRequest
import com.alistairfink.betteropendrive.requestModels.FolderTrashRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class OpenDriveFolderApiClient(private val context: Context)
{
    private val compositeDisposable = CompositeDisposable()
    // TODO : Refactor these exceptions in here and file client too
    fun cut(folderId: String, destinationId: String)
    {
        var sharedPreferences = SharedPreferencesClient(context)
        var sessionId = sharedPreferences.getString(SharedPreferenceConstants.SessionId)!!
        var request = FolderMoveCopyRequest (
                SessionId = sessionId,
                FolderId = folderId,
                DestinationFolderId = destinationId,
                Move = true
        )
        moveCopy(request)
    }

    fun copy(folderId: String, destinationId: String)
    {
        var sharedPreferences = SharedPreferencesClient(context)
        var sessionId = sharedPreferences.getString(SharedPreferenceConstants.SessionId)!!
        var request = FolderMoveCopyRequest (
                SessionId = sessionId,
                FolderId = folderId,
                DestinationFolderId = destinationId,
                Move = false
        )
        moveCopy(request)
    }

    fun rename(folderId: String, newName: String)
    {
        var sharedPreferences = SharedPreferencesClient(context)
        var sessionId = sharedPreferences.getString(SharedPreferenceConstants.SessionId)!!
        var request = FolderRenameRequest (
                SessionId = sessionId,
                FolderId = folderId,
                NewName = newName
        )
        var repository = OpenDriveRepositoryProvider.provideOpenDriveRepository()
        compositeDisposable.add(
                repository.renameFolder(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            if (result.Name != newName)
                            {
                                throw Exception("Folder Rename Operation Did Not Complete Properly")
                            }
                        }, { error ->
                            error.printStackTrace()
                        })
        )
    }

    fun trash(folderId: String)
    {
        var sharedPreferences = SharedPreferencesClient(context)
        var sessionId = sharedPreferences.getString(SharedPreferenceConstants.SessionId) as String
        var repository = OpenDriveRepositoryProvider.provideOpenDriveRepository()
        var request = FolderTrashRequest(
                SessionId = sessionId,
                FolderId = folderId
        )
        compositeDisposable.add(
                repository.trashFolder(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            if (result.DirUpdateTime <= 0)
                            {
                                throw Exception("Trash Folder Operation Did Not Complete Properly")
                            }
                        }, { error ->
                            error.printStackTrace()
                        })
        )
    }

    fun getFolder(folderId: String, callBack: (FolderModel) -> Unit)
    {
        var sharedPreferences = SharedPreferencesClient(context)
        var sessionId = sharedPreferences.getString(SharedPreferenceConstants.SessionId) as String
        var repository = OpenDriveRepositoryProvider.provideOpenDriveRepository()
        compositeDisposable.add(
                repository.folderList(sessionId, folderId)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            var resultData =
                                    if (folderId == "0")
                                    {
                                        FolderModelHelper.toFolderModel(result, true)
                                    }
                                    else
                                    {
                                        FolderModelHelper.toFolderModel(result)
                                    }

                            var internalStorage = InternalStorageClient(this.context)
                            internalStorage.writeFolder(resultData, InternalStroageConstants.FolderPrefix + folderId)
                            callBack.invoke(resultData)
                        }, { error ->
                            error.printStackTrace()
                        })
        )
    }

    private fun moveCopy(request: FolderMoveCopyRequest)
    {
        var repository = OpenDriveRepositoryProvider.provideOpenDriveRepository()
        compositeDisposable.add(
                repository.moveCopyFolder(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            if (result.FolderID != request.FolderId)
                            {
                                throw Exception("Move/Copy Folder Operation Did Not Complete Successfully")
                            }
                        },{ error ->
                            error.printStackTrace()
                        })
        )
    }
}