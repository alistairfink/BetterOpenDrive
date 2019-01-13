package com.alistairfink.betteropendrive.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.TextView
import com.alistairfink.betteropendrive.R
import com.alistairfink.betteropendrive.helpers.OpenDriveFolderApiClient

class CreateFolderDialog: DialogFragment()
{
    private lateinit var createFolderDialogListener: IDialogListener

    companion object
    {
        fun newInstance(parentFolderId: String): CreateFolderDialog
        {
            var fragment = CreateFolderDialog()
            var args = Bundle()
            fragment.arguments = args
            args.putString("parentFolderId", parentFolderId)
            return fragment
        }
    }

    fun setCreateFolderDialogListener(listener: IDialogListener)
    {
        createFolderDialogListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.dialog_create_folder, null)

        var name = view.findViewById(R.id.dialog_create_folder_name) as TextView

        builder
                .setPositiveButton("CONFIRM") { dialog, _ ->
                    var newName = name.text
                    onClickConfirm(dialog, newName.toString())
                }
                .setNegativeButton("CANCEL") { dialog, _ ->
                    dialog.cancel()
                }

        builder.setView(view)
        return builder.create()
    }

    private fun onClickConfirm(dialog: DialogInterface, newName: String)
    {
        var parentId = arguments.getString("parentFolderId")
        var openDriveFolderClient = OpenDriveFolderApiClient(this.context)
        openDriveFolderClient.createFolder(newName, parentId)
        dialog.dismiss()
        createFolderDialogListener.onSuccess(dialog)
    }
}