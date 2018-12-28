package com.alistairfink.betteropendrive.dialogs

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.EditText
import android.widget.TextView
import com.alistairfink.betteropendrive.R
import com.alistairfink.betteropendrive.helpers.OpenDriveFileApiClient

class RenameDialog: DialogFragment()
{
    private lateinit var renameDialogListener: RenameDialogListener

    companion object
    {
        fun newInstance(itemName: String, id: String, isFile: Boolean): RenameDialog
        {
            var fragment = RenameDialog()
            var args = Bundle()
            fragment.arguments = args
            args.putString("itemName", itemName)
            args.putString("id",  id)
            args.putBoolean("isFile", isFile)
            return fragment
        }
    }

    fun setRenameDialogListener(listener: RenameDialogListener)
    {
        renameDialogListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.dialog_rename, null)

        var isFile = arguments.getBoolean("isFile")
        val title = view.findViewById(R.id.dialog_rename_title) as TextView
        if (isFile)
        {
            title.text = "Rename File"
        }
        else
        {
            title.text = "Rename Folder"
        }

        var itemName = arguments.getString("itemName")
        var newTitle = view.findViewById(R.id.dialog_rename_newTitle) as EditText
        newTitle.setText(itemName)

        builder
                .setPositiveButton("RENAME") { dialog, _ ->
                    var newName = newTitle.text
                    onClickConfirm(dialog, newName.toString())
                }
                .setNegativeButton("CANCEL") { dialog, _ ->
                    dialog.cancel()
                }

        builder.setView(view)
        val dialog = builder.create()
        return dialog
    }

    private fun onClickConfirm(dialog: DialogInterface, newName: String)
    {
        //var newTitle = (view?.findViewById(R.id.dialog_rename_newTitle) as EditText).text
        var id = arguments.getString("id")
        var isFile = arguments.getBoolean("isFile")
        if (isFile)
        {
            var openDriveFileClient = OpenDriveFileApiClient(this.context)
            openDriveFileClient.rename(newName, id)
            dialog.dismiss()
        }
        else
        {

        }
        renameDialogListener.onSuccess(dialog)
    }

    interface RenameDialogListener
    {
        fun onSuccess(dialog: DialogInterface)
    }
}