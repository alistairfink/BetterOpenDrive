package com.alistairfink.betteropendrive

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.alistairfink.betteropendrive.apiService.repositories.OpenDriveRepositoryProvider
import com.alistairfink.betteropendrive.dataModels.FileModel
import com.alistairfink.betteropendrive.dataModels.FolderModel
import com.alistairfink.betteropendrive.dataModels.FolderModelHelper
import com.alistairfink.betteropendrive.dataModels.SubFolderModel
import com.alistairfink.betteropendrive.helpers.InternalStorageClient
import com.alistairfink.betteropendrive.helpers.SharedPreferencesClient
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.folder_browser_item.view.*
import kotlinx.android.synthetic.main.fragment_folder_browser.*
import java.text.SimpleDateFormat

class FolderBrowserFragment : Fragment()
{
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var listView: ListView

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
        var renderList: MutableList<Any> = mutableListOf()
        renderList.addAll(folder.Folders)
        renderList.addAll(folder.Files)
        var adapter = FolderBrowserItemAdapter(this.context, renderList)
        folder_browser_list.adapter = adapter
        folder_browser_list.setOnItemClickListener { adapter, view, i, l ->
            var folder = adapter.getItemAtPosition(i)
            if (folder is SubFolderModel)
            {
                onClickFolder(folder)
            }
        }
    }

    private fun onClickFolder(folder: SubFolderModel)
    {
        var fragmentTransaction = fragmentManager.beginTransaction()
        var fragment = FolderBrowserFragment.newInstance(folder.FolderId)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.hide(this)
        fragmentTransaction.add(R.id.content_frame, fragment)
        fragmentTransaction.commit()
    }
}

class FolderBrowserItemAdapter(val context: Context, var data: List<Any>) : BaseAdapter()
{
    // TODO : Convert this to RecyclerView
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(i: Int, vire: View?, parent: ViewGroup?): View {
        var listItem = getItem(i)
        val item = inflater.inflate(R.layout.folder_browser_item, parent, false)

        if (listItem is SubFolderModel) {
            item.icon.setImageResource(R.drawable.ic_folder_open)
            item.name_layout.name.text = listItem.Name
            var date = "Modified: " + SimpleDateFormat("MMM dd yyyy").format(listItem.DateModified)
            item.name_layout.date.text = date
            item.menu.setOnClickListener {
                onClickFolderMenu(listItem)
            }
        }
        else if (listItem is FileModel)
        {
            Picasso.with(context).load(Uri.parse(listItem.Thumbnail)).into(item.icon)
            item.name_layout.name.text = listItem.Name
            var date = "Modified: " + SimpleDateFormat("MMM dd yyyy").format(listItem.DateModified)
            item.name_layout.date.text = date
            item.menu.setOnClickListener {
                onClickFileMenu(listItem)
            }
        }
        return item
    }

    override fun getItem(i: Int): Any {
        return data[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getCount(): Int {
        return this.data.size
    }

    private fun onClickFolderMenu(folder: SubFolderModel)
    {
        Toast.makeText(context, "Folder Clicked", Toast.LENGTH_SHORT).show()
    }

    private fun onClickFileMenu(folder: FileModel)
    {
        Toast.makeText(context, "File Clicked", Toast.LENGTH_SHORT).show()
    }


}