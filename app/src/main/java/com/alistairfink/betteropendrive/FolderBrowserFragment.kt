package com.alistairfink.betteropendrive

import android.content.Context
import android.content.DialogInterface
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
import android.widget.PopupMenu
import android.widget.Toast
import com.alistairfink.betteropendrive.apiService.repositories.OpenDriveRepositoryProvider
import com.alistairfink.betteropendrive.dataModels.FileModel
import com.alistairfink.betteropendrive.dataModels.FolderModel
import com.alistairfink.betteropendrive.dataModels.FolderModelHelper
import com.alistairfink.betteropendrive.dataModels.SubFolderModel
import com.alistairfink.betteropendrive.dialogs.IDialogListener
import com.alistairfink.betteropendrive.dialogs.RenameDialog
import com.alistairfink.betteropendrive.dialogs.TrashDialog
import com.alistairfink.betteropendrive.helpers.InternalStorageClient
import com.alistairfink.betteropendrive.helpers.OpenDriveFileApiClient
import com.alistairfink.betteropendrive.helpers.SharedPreferencesClient
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.folder_browser_item.view.*
import kotlinx.android.synthetic.main.fragment_folder_browser.*
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class FolderBrowserFragment : Fragment(), IDialogListener
{
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

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
        folder_browser_refresh.setOnRefreshListener {
            getFolder(arguments.getString("folderId"))
        }
        if (folder != null)
        {
            renderViews(folder)
        }

        folder_browser_fab.setOnClickListener { v ->
            addButton(v)
        }
        // TODO : REMOVE THIS COMMENT WHEN DONE TESTING
        getFolder(folderId)
    }

    private fun getFolder(folderId: String)
    {
        folder_browser_refresh.isRefreshing = true
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
                                        FolderModelHelper.toFolderModel(result, true)
                                    }
                                    else
                                    {
                                        FolderModelHelper.toFolderModel(result)
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
        var adapter = FolderBrowserItemAdapter(this.context, renderList, { _item -> onClickListener(_item)}, { _item, _view -> onClickMenuListener(_item, _view)})
        folder_browser_list.layoutManager = LinearLayoutManager(this.context)
        folder_browser_list.adapter = adapter
        folder_browser_refresh.isRefreshing = false
    }

    private fun onClickListener(item: Any)
    {
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator())
        {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        }

        if (item is SubFolderModel)
        {
            var fragmentTransaction = fragmentManager.beginTransaction()
            var fragment = FolderBrowserFragment.newInstance(item.FolderId)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.hide(this)
            fragmentTransaction.add(R.id.content_frame, fragment)
            fragmentTransaction.commit()
        }
        else if (item is FileModel)
        {
            var fileClient = OpenDriveFileApiClient(this.context)
            fileClient.download(item) { _file, _data -> fileOpen(_file, _data) }
        }
    }

    private fun fileOpen(file: FileModel, data: ByteArray)
    {
        val outputDir = this.context.externalCacheDir
        val outputFile = File.createTempFile(file.FileId, ".${file.Extension}", outputDir)

        var fos = FileOutputStream(outputFile.path)

        fos.write(data)
        fos.close()

        var fragmentTransaction = fragmentManager.beginTransaction()
        var fragment = FilePreview.newInstance(outputFile.path)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.hide(this)
        fragmentTransaction.add(R.id.content_frame, fragment)
        fragmentTransaction.commit()
    }

    private fun addButton(view: View)
    {
        var popup = PopupMenu(this.context, view)
        popup.menuInflater.inflate(R.menu.add_button_popup_menu, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when(item.itemId)
            {
                R.id.add_button_popup_add_file ->
                {

                }
                R.id.add_button_popup_add_folder ->
                {

                }
            }
            true
        }

        popup.show()
    }

    private fun onClickMenuListener(item: Any, view: View)
    {
        if (item is SubFolderModel)
        {
            onClickFolderMenu(item, view)
        }
        else if (item is FileModel)
        {
            onClickFileMenu(item, view)
        }
    }


    private fun onClickFolderMenu(folder: SubFolderModel, view: View)
    {
        var newFileName = "animated.gif"
        var folderId = "123"
        var openDriveFileApiClient = OpenDriveFileApiClient(this.context)
        openDriveFileApiClient.rename(newFileName, folderId)

        Toast.makeText(this.context, "Folder Clicked " + folder.FolderId, Toast.LENGTH_SHORT).show()
    }

    private fun onClickFileMenu(file: FileModel, view: View)
    {
        var popup = PopupMenu(this.context, view.menu)
        popup.menuInflater.inflate(R.menu.folder_browser_popup_menu, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when(item.itemId)
            {
                R.id.folder_browser_popup_cut ->
                {

                }
                R.id.folder_browser_popup_copy ->
                {

                }
                R.id.folder_browser_popup_rename ->
                {
                    val ft = fragmentManager.beginTransaction()
                    val prev = fragmentManager.findFragmentByTag("dialog")
                    if (prev != null)
                    {
                        ft.remove(prev)
                    }
                    ft.addToBackStack(null)
                    val dialogFragment = RenameDialog.newInstance(file.Name, file.FileId, true)
                    dialogFragment.setRenameDialogListener(this)
                    dialogFragment.show(ft, "dialog")
                }
                R.id.folder_browser_popup_trash ->
                {
                    val ft = fragmentManager.beginTransaction()
                    val prev = fragmentManager.findFragmentByTag("dialog")
                    if (prev != null)
                    {
                        ft.remove(prev)
                    }
                    ft.addToBackStack(null)
                    val dialogFragment = TrashDialog.newInstance(file.Name, file.FileId, true)
                    dialogFragment.setTrashDialogListener(this)
                    dialogFragment.show(ft, "dialog")
                }
                R.id.folder_browser_popup_properties ->
                {

                }
            }
            true
        }

        popup.show()
    }

    override fun onSuccess(dialog: DialogInterface)
    {
        var folderId = arguments.getString("folderId")
        getFolder(folderId)
    }

    class FolderBrowserItemAdapter(
            private val context: Context,
            var data: List<Any>,
            private val clickListener: (Any) -> Unit,
            private val menuListener: (Any, View) -> Unit
    ) : RecyclerView.Adapter<FolderBrowserItemHolder>()
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
            }
            else if (listItem is FileModel)
            {
                Picasso.with(context).load(Uri.parse(listItem.Thumbnail)).into(holder.item.icon)
                holder.item.name_layout.name.text = listItem.Name
                var date = "Modified: " + SimpleDateFormat("MMM dd yyyy", Locale.getDefault()).format(listItem.DateModified)
                holder.item.name_layout.date.text = date
            }
            holder.item.menu.setOnClickListener { menuListener.invoke(listItem, holder.item) }
            holder.item.setOnClickListener { clickListener.invoke(listItem) }
        }
    }

    class FolderBrowserItemHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val item = view.folder_browser_item!!
    }
}