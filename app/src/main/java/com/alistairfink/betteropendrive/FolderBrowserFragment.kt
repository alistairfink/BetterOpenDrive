package com.alistairfink.betteropendrive

import android.content.Context
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
import android.widget.Toast
import com.alistairfink.betteropendrive.apiService.repositories.OpenDriveRepositoryProvider
import com.alistairfink.betteropendrive.dataModels.FileModel
import com.alistairfink.betteropendrive.dataModels.FolderModel
import com.alistairfink.betteropendrive.dataModels.FolderModelHelper
import com.alistairfink.betteropendrive.dataModels.SubFolderModel
import com.alistairfink.betteropendrive.helpers.InternalStorageClient
import com.alistairfink.betteropendrive.helpers.SharedPreferencesClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_folder_browser.*
import java.text.SimpleDateFormat

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
                                        FolderModelHelper.toDataModel(result, true)
                                    }
                                    else
                                    {
                                        FolderModelHelper.toDataModel(result)
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
        folder_browser_layout.removeAllViews()
        for (i in folder.Folders.indices)
        {
            var layout = createFolder(folder.Folders[i], this.context, i)
            folder_browser_layout.addView(layout)

            var layout2 = LinearLayout(this.context)
            layout2.id =  i+100
            layout2.setBackgroundColor(Color.parseColor("#000000"))
            layout2.layoutParams = ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5)
            folder_browser_layout.addView(layout2)
        }
        for (i in folder.Files.indices)
        {
            var layout = createFile(folder.Files[i], this.context, i)
            folder_browser_layout.addView(layout)

            var layout2 = LinearLayout(this.context)
            layout2.id =  i+100
            layout2.setBackgroundColor(Color.parseColor("#000000"))
            layout2.layoutParams = ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5)
            folder_browser_layout.addView(layout2)
        }
    }

    private fun createFolder(folder: SubFolderModel, context: Context, index: Int): LinearLayout
    {
        var layout = LinearLayout(context)
        val height = folder_browser_header.height
        var width = folder_browser_header.width
        // Layout Stuff
        layout.id = index
        layout.setBackgroundColor(Color.parseColor("#FFFFFF"))
        layout.layoutParams = ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)
        layout.orientation = LinearLayout.HORIZONTAL
        layout.setOnClickListener {
            onClickFolder(folder)
        }
        // Icon
        var icon = ImageView(context)
        icon.setImageResource(R.drawable.ic_folder_open)
        icon.setPadding(20, 0, 20, 0)
        icon.layoutParams = ViewGroup.LayoutParams(120, height)
        layout.addView(icon)
        // Title
        var nameLayout = LinearLayout(context)
        nameLayout.layoutParams = ViewGroup.LayoutParams(width-240, height)
        nameLayout.orientation = LinearLayout.VERTICAL
        var title = TextView(context)
        title.text = folder.Name
        title.textSize = 15f
        title.gravity = Gravity.TOP
        title.setPadding(0, 40, 0, 0)
        title.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height-35)
        nameLayout.addView(title)
        var date = TextView(context)
        date.text = "Modified: " + SimpleDateFormat("MMM dd yyyy").format(folder.DateModified)
        date.textSize = 10f
        date.gravity = Gravity.BOTTOM
        date.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 35)
        date.setPadding(0, 0, 0, 5)
        nameLayout.addView(date)
        layout.addView(nameLayout)
        // Menu Icon
        var menu = ImageView(context)
        menu.setImageResource(R.drawable.ic_menu)
        menu.setPadding(20, 0, 20, 0)
        menu.layoutParams = ViewGroup.LayoutParams(120, height)
        menu.setOnClickListener {
            onClickFolderMenu(folder)
        }
        layout.addView(menu)
        return layout
    }

    private fun createFile(file: FileModel, context: Context, index: Int): LinearLayout
    {

        var layout = LinearLayout(context)
        val height = folder_browser_header.height
        var width = folder_browser_header.width
        // Layout Stuff
        layout.id = index
        layout.setBackgroundColor(Color.parseColor("#FFFFFF"))
        layout.layoutParams = ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)
        layout.orientation = LinearLayout.HORIZONTAL
        layout.setOnClickListener {
            onClickFile(file)
        }
        // Icon
        // TODO : Use Icons
        var icon = ImageView(context)
        icon.setImageResource(R.drawable.ic_file)
        icon.setPadding(20, 0,20,0)
        icon.layoutParams = ViewGroup.LayoutParams(120, height)
        layout.addView(icon)
        // Title
        var nameLayout = LinearLayout(context)
        nameLayout.layoutParams = ViewGroup.LayoutParams(width-240, height)
        nameLayout.orientation = LinearLayout.VERTICAL
        var title = TextView(context)
        title.text = file.Name
        title.textSize = 15f
        title.gravity = Gravity.TOP
        title.setPadding(0, 40, 0, 0)
        title.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height-35)
        nameLayout.addView(title)
        var date = TextView(context)
        date.text = "Modified: " + SimpleDateFormat("MMM dd yyyy").format(file.DateModified)
        date.textSize = 10f
        date.gravity = Gravity.BOTTOM
        date.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 35)
        date.setPadding(0, 0, 0, 5)
        nameLayout.addView(date)
        layout.addView(nameLayout)
        // Menu Icon
        var menu = ImageView(context)
        menu.setImageResource(R.drawable.ic_menu)
        menu.setPadding(20, 0, 20, 0)
        menu.layoutParams = ViewGroup.LayoutParams(120, height)
        menu.setOnClickListener {
            onClickFileMenu(file)
        }
        layout.addView(menu)
        return layout
    }

    private fun onClickFolderMenu(folder: SubFolderModel)
    {

    }

    private fun onClickFolder(folder: SubFolderModel)
    {
        var fragment = FolderBrowserFragment.newInstance(folder.FolderId)
      /*  val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.content_frame, fragment)
        ft.commit()
*/
    }

    private fun onClickFileMenu(file: FileModel)
    {

    }

    private fun onClickFile(file: FileModel)
    {

    }
}