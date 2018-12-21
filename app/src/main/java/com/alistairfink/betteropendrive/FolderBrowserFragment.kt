package com.alistairfink.betteropendrive

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
import java.util.*

class FolderBrowserFragment : Fragment()
{
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var listView: ListView

    companion object
    {
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

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        var folderId = arguments.getString("folderId")
        var internalStorage = InternalStorageClient(this.context)
        var folder = internalStorage.readFolder(InternalStroageConstants.FolderPrefix + folderId)
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
                            internalStorage.writeFolder(resultData, InternalStroageConstants.FolderPrefix + folderId)
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
        var adapter = FolderBrowserItemAdapter(this.context, renderList) { _item -> onClickListener(_item) }
        folder_browser_list.layoutManager = LinearLayoutManager(this.context)
        folder_browser_list.adapter = adapter
    }

    private fun onClickListener(item: Any)
    {
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator())
        {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        }

        if (item is SubFolderModel) {
            var fragmentTransaction = fragmentManager.beginTransaction()
            var fragment = FolderBrowserFragment.newInstance(item.FolderId)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.hide(this)
            fragmentTransaction.add(R.id.content_frame, fragment)
            fragmentTransaction.commit()
        }
        else if (item is FileModel)
        {
            // TODO : Open file here
            Toast.makeText(this.context, "You did a thing " + item.FileId, Toast.LENGTH_SHORT).show()
        }
    }

    class FolderBrowserItemAdapter(private val context: Context, var data: List<Any>, private val listener: (Any) -> Unit) : RecyclerView.Adapter<FolderBrowserItemHolder>()
    {
        override fun getItemCount(): Int
        {
            return data.size
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): FolderBrowserItemHolder
        {
            return FolderBrowserItemHolder(LayoutInflater.from(context).inflate(R.layout.folder_browser_item, parent, false))
        }

        override fun onBindViewHolder(holder: FolderBrowserItemHolder?, i: Int)
        {
            var listItem = data[i]
            if (holder == null)
            {
                return
            }

            if (listItem is SubFolderModel)
            {
                holder.item.icon.setImageResource(R.drawable.ic_folder_open)
                holder.item.name_layout.name.text = listItem.Name
                var date = "Modified: " + SimpleDateFormat("MMM dd yyyy", Locale.getDefault()).format(listItem.DateModified)
                holder.item.name_layout.date.text = date
                holder.item.menu.setOnClickListener {
                    onClickFolderMenu(listItem)
                }
                holder.item.setOnClickListener { listener.invoke(listItem) }
            }
            else if (listItem is FileModel)
            {
                Picasso.with(context).load(Uri.parse(listItem.Thumbnail)).into(holder.item.icon)
                holder.item.name_layout.name.text = listItem.Name
                var date = "Modified: " + SimpleDateFormat("MMM dd yyyy", Locale.getDefault()).format(listItem.DateModified)
                holder.item.name_layout.date.text = date
                holder.item.menu.setOnClickListener {
                    onClickFileMenu(listItem)
                }
                holder.item.setOnClickListener { listener.invoke(listItem) }
            }
        }

        private fun onClickFolderMenu(folder: SubFolderModel)
        {
            Toast.makeText(context, "Folder Clicked " + folder.FolderId, Toast.LENGTH_SHORT).show()
        }

        private fun onClickFileMenu(file: FileModel)
        {
            Toast.makeText(context, "File Clicked " + file.FileId, Toast.LENGTH_SHORT).show()
        }
    }

    class FolderBrowserItemHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val item = view.folder_browser_item!!
    }
}