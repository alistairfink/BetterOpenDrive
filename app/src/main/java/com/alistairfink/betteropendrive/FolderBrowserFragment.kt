package com.alistairfink.betteropendrive

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.alistairfink.betteropendrive.apiService.repositories.OpenDriveRepositoryProvider
import com.alistairfink.betteropendrive.dataModels.FolderModel
import com.alistairfink.betteropendrive.dataModels.FolderModelHelper
import com.alistairfink.betteropendrive.dataModels.SubFolderModel
import com.alistairfink.betteropendrive.helpers.InternalStorageClient
import com.alistairfink.betteropendrive.helpers.SharedPreferencesClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_folder_browser.*

class FolderBrowserFragment : Fragment()
{
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    companion object {
        fun newInstance(folderId: String): FolderBrowserFragment
        {
            var fragment = FolderBrowserFragment()
            var args = Bundle()
            args.putString("folderId", folderId)
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanteState: Bundle?): View
    {
        return inflater.inflate(R.layout.fragment_folder_browser, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var folderId = arguments.getString("folderId")
        var internalStorage = InternalStorageClient(this.context)
        var folder = internalStorage.readFolder(InternalStroageConstants.FolderPrefix+folderId)
        if (folder != null)
        {
            renderViews(folder)
        }
        getFolder(folderId)
    }

    private fun getFolder(folderId: String)
    {
        var sharedPreferences = SharedPreferencesClient(this.context)
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
                                        FolderModelHelper.ToDataModel(result, true)
                                    }
                                    else
                                    {
                                        FolderModelHelper.ToDataModel(result)
                                    }

                            var internalStorage = InternalStorageClient(this.context)
                            internalStorage.writeFolder(resultData, InternalStroageConstants.FolderPrefix+folderId)
                            renderViews(resultData)
                        }, { error ->
                            error.printStackTrace()
                        })
        )
    }

    private fun renderViews(folder: FolderModel)
    {
        folder_title.text = folder.Name
        // TODO : Change from listing textviews to other view
        folder_browser_layout.removeAllViews()
        val height = folder_browser_header.height
        var width = folder_browser_header.width
        for (i in folder.Folders.indices)
        {
            var layout = LinearLayout(this.context)
            layout.id =  i
            layout.setBackgroundColor(Color.parseColor("#FFFFFF"))
            layout.layoutParams = ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)
            layout.orientation = LinearLayout.HORIZONTAL
            // Icon
            var icon = ImageView(this.context)
            icon.setImageResource(R.drawable.ic_folder)
            icon.layoutParams = ViewGroup.LayoutParams(width*0.3f as Int, height)
            layout.addView(icon)
            // Title
            var title = TextView(this.context)
            title.text = folder.Folders[i].Name
            title.textSize = 20f
            title.gravity = Gravity.CENTER_VERTICAL
            title.layoutParams = ViewGroup.LayoutParams(width*0.7f as Int, height)
            layout.addView(title)
            // Menu Icon

            folder_browser_layout.addView(layout)

            var layout2 = LinearLayout(this.context)
            layout2.id =  i+100
            layout2.setBackgroundColor(Color.parseColor("#000000"))
            layout2.layoutParams = ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5)
            folder_browser_layout.addView(layout2)
        }
    }

}