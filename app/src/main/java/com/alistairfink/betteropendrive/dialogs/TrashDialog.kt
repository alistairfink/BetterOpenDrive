package com.alistairfink.betteropendrive.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.TextView
import com.alistairfink.betteropendrive.R
import com.alistairfink.betteropendrive.helpers.OpenDriveFileApiClient

class TrashDialog: DialogFragment()
{

    private lateinit var trashDialogListener: IDialogListener

    companion object
    {
        fun newInstance(itemName: String, id: String, isFile: Boolean): TrashDialog
        {
            var fragment = TrashDialog()
            var args = Bundle()
            fragment.arguments = args
            args.putString("itemName", itemName)
            args.putString("id",  id)
            args.putBoolean("isFile", isFile)
            return fragment
        }
    }

    fun setTrashDialogListener(listener: IDialogListener)
    {
        trashDialogListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.dialog_trash, null)

        var isFile = arguments.getBoolean("isFile")
        val title = view.findViewById(R.id.dialog_copy_move_title) as TextView
        if (isFile)
        {
            title.text = "Send File to Trash?"
        }
        else
        {
            title.text = "Send Folder to Trash?"
        }

        //
        var itemName = arguments.getString("itemName")
        var newTitle = view.findViewById(R.id.dialog_trash_description) as TextView
        newTitle.text = "Are you sure you want to send $itemName to the trash? This action can be undone later."

        builder
                .setPositiveButton("CONFIRM") { dialog, _ ->
                    onClickConfirm(dialog)
                }
                .setNegativeButton("CANCEL") { dialog, _ ->
                    dialog.cancel()
                }

        builder.setView(view)
        return builder.create()
    }

    private fun onClickConfirm(dialog: DialogInterface)
    {
        var id = arguments.getString("id")
        var isFile = arguments.getBoolean("isFile")
        if (isFile)
        {
            var openDriveFileClient = OpenDriveFileApiClient(this.context)
            openDriveFileClient.trash(id)
            dialog.dismiss()
        }
        else
        {

        }
        trashDialogListener.onSuccess(dialog)
    }

}