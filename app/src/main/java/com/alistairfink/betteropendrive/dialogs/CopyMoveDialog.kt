package com.alistairfink.betteropendrive.dialogs

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.alistairfink.betteropendrive.InternalStroageConstants
import com.alistairfink.betteropendrive.R
import com.alistairfink.betteropendrive.SharedPreferenceConstants
import com.alistairfink.betteropendrive.apiService.repositories.OpenDriveRepositoryProvider
import com.alistairfink.betteropendrive.dataModels.FileModel
import com.alistairfink.betteropendrive.dataModels.FolderModel
import com.alistairfink.betteropendrive.dataModels.FolderModelHelper
import com.alistairfink.betteropendrive.dataModels.SubFolderModel
import com.alistairfink.betteropendrive.helpers.InternalStorageClient
import com.alistairfink.betteropendrive.helpers.OpenDriveFileApiClient
import com.alistairfink.betteropendrive.helpers.SharedPreferencesClient
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.copy_move_item.view.*

class CopyMoveDialog: DialogFragment()
{
    private lateinit var copyMoveDialogListener: IDialogListener
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var dialog: AlertDialog
    private lateinit var currentFolderId: String
    private lateinit var currentFolder: FolderModel

    companion object
    {
        fun newInstance(itemName: String, id: String, isFile: Boolean, copy: Boolean): CopyMoveDialog
        {
            var fragment = CopyMoveDialog()
            var args = Bundle()
            fragment.arguments = args
            args.putString("itemName", itemName)
            args.putString("id", id)
            args.putBoolean("isFile", isFile)
            args.putBoolean("copy", copy)
            return fragment
        }
    }


    fun setCopyMoveDialogListener(listener: IDialogListener)
    {
        copyMoveDialogListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.dialog_copy_move, null)


        var isFile = arguments.getBoolean("isFile")
        var copy = arguments.getBoolean("copy")
        var copyMoveText = if (copy) { "Copy" } else { "Move" } + " " + if (isFile) { "File" } else { "Folder" }
        val title = view.findViewById(R.id.dialog_copy_move_title) as TextView
        title.text = copyMoveText

        var confirmButton = view.findViewById(R.id.dialog_copy_move_confirm) as Button
        confirmButton.setOnClickListener {
            onClickConfirm()
        }
        var cancelButton = view.findViewById(R.id.dialog_copy_move_cancel) as Button
        cancelButton.setOnClickListener {
            onClickCancel()
        }

        val copyMoveList = view.findViewById(R.id.copy_move_list) as RecyclerView
        val parentIcon = view.findViewById(R.id.parent_folder_icon) as ImageView
        getFolder("0", copyMoveList, parentIcon)

        builder.setView(view)
        dialog =  builder.create()
        return dialog
    }

    private fun onClickConfirm()
    {
        var isFile = arguments.getBoolean("isFile")
        var name = arguments.getString("itemName")
        if (isFile)
        {
            if(currentFolder.Files.any { a -> a.Name == name} )
            {
                // TODO : OPEN DIALOG
                //val ft = fragmentManager.beginTransaction()
                //ft.addToBackStack(null)
               // val dialogFragment = TrashDialog.newInstance(file.Name, file.FileId, true)
                //dialogFragment.setTrashDialogListener(this)
                //dialogFragment.show(ft, "dialog")
            }
            else
            {
                actionFile()
            }
        }
        else
        {

        }
    }

    fun onClickCancel()
    {
        dialog.cancel()
    }

    private fun actionFile()
    {
        var copy = arguments.getBoolean("copy")
        var id = arguments.getString("id")
        var openDriveFileClient = OpenDriveFileApiClient(this.context)
        if (copy)
        {
            openDriveFileClient.copy(id, currentFolderId)
        }
        else
        {
            openDriveFileClient.move(id, currentFolderId)
        }
        dialog.dismiss()
        copyMoveDialogListener.onSuccess(dialog)
    }

    private fun getFolder(folderId: String, copyMoveList: RecyclerView, parentIcon: ImageView)
    {
        currentFolderId = folderId
        var internalStorage = InternalStorageClient(this.context)
        var folder = internalStorage.readFolder(InternalStroageConstants.FolderPrefix + folderId)
        if (folder != null)
        {
            renderViews(folder, copyMoveList, parentIcon)
        }

        // TODO: Abstract this into folder client since it's used in multiple places
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
                            renderViews(resultData, copyMoveList, parentIcon)
                        }, { error ->
                            error.printStackTrace()
                        })
        )
    }

    private fun renderViews(folder: FolderModel, copyMoveList: RecyclerView, parentIcon: ImageView)
    {
        currentFolder = folder
        var renderList: MutableList<Any> = mutableListOf()
        // TODO : Refactor this so it doesn't happen for parent folder
        parentIcon.setOnClickListener {
            var copyMoveList = dialog.findViewById<RecyclerView>(R.id.copy_move_list)!!
            getFolder(folder.ParentFolderId!!, copyMoveList, parentIcon)
        }

        renderList.addAll(folder.Folders)
        renderList.addAll(folder.Files)
        var adapter = CopyMoveItemAdapter(this.context, renderList) { _item -> onClickListener(_item) }
        copyMoveList.layoutManager = LinearLayoutManager(this.context)
        copyMoveList.adapter = adapter
    }

    private fun onClickListener(item: SubFolderModel)
    {
        var copyMoveList = dialog.findViewById<RecyclerView>(R.id.copy_move_list)!!
        var parentIcon = dialog.findViewById<ImageView>(R.id.parent_folder_icon)!!
        getFolder(item.FolderId, copyMoveList, parentIcon)
    }

    class CopyMoveItemAdapter(
            private val context: Context,
            var data: List<Any>,
            private val clickListener: (SubFolderModel) -> Unit
    ) : RecyclerView.Adapter<CopyMoveItemHolder>()
    {
        override fun getItemCount(): Int
        {
            return data.size
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CopyMoveItemHolder
        {
            return CopyMoveItemHolder(LayoutInflater.from(context).inflate(R.layout.copy_move_item, parent, false))
        }

        override fun onBindViewHolder(holder: CopyMoveItemHolder?, i: Int)
        {
            var listItem = data[i]
            if (holder == null)
            {
                return
            }

            if (listItem is SubFolderModel)
            {
                holder.item.icon.setImageResource(R.drawable.ic_folder_open)
                holder.item.setOnClickListener { clickListener.invoke(listItem) }
                holder.item.name.text = listItem.Name
            }
            else if (listItem is FileModel)
            {
                Picasso.with(context).load(Uri.parse(listItem.Thumbnail)).into(holder.item.icon)
                holder.item.name.text = listItem.Name
            }
        }
    }

    class CopyMoveItemHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val item = view.copy_move_item!!
    }
}
