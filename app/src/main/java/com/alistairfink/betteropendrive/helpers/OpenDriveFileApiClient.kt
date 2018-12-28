package com.alistairfink.betteropendrive.helpers

import android.content.Context
import com.alistairfink.betteropendrive.SharedPreferenceConstants
import com.alistairfink.betteropendrive.apiService.repositories.OpenDriveRepositoryProvider
import com.alistairfink.betteropendrive.dataModels.FileModel
import com.alistairfink.betteropendrive.requestModels.FileRenameRequest
import com.alistairfink.betteropendrive.requestModels.FileTrashRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

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
        var sessionId = sharedPreferences.getString(SharedPreferenceConstants.SessionId)
        if (sessionId != null)
        {
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
        throw Exception("Bad Session ID")
    }

    fun rename(newName: String, fileId: String)
    {
        var sharedPreferences = SharedPreferencesClient(context)
        var sessionId = sharedPreferences.getString(SharedPreferenceConstants.SessionId)
        if (sessionId != null)
        {
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
        throw Exception("Bad Session ID")
    }
}