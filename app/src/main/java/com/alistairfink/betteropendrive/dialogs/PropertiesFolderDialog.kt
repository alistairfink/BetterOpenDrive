package com.alistairfink.betteropendrive.dialogs

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import com.alistairfink.betteropendrive.R
import com.alistairfink.betteropendrive.dataModels.SubFolderModel
import com.google.gson.Gson

class PropertiesFolderDialog: DialogFragment()
{
    private lateinit var _folder: SubFolderModel
    companion object
    {
        fun newInstance(folder: SubFolderModel): PropertiesFolderDialog
        {
            var fragment = PropertiesFolderDialog()
            var args = Bundle()
            fragment.arguments = args
            args.putString("folder", Gson().toJson(folder))
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.dialog_properties_folder, null)

        var folderString = arguments.getString("folder")
        var folder = Gson().fromJson(folderString, SubFolderModel::class.java)
        _folder = folder

        /*
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
*/
        builder
                .setPositiveButton("CONFIRM") { dialog, _ ->
                    //onClickConfirm(dialog)
                }
                .setNegativeButton("CANCEL") { dialog, _ ->
                    dialog.cancel()
                }

        builder.setView(view)
        return builder.create()
    }
    /*

    private fun onClickConfirm(dialog: DialogInterface)
    {
        var id = arguments.getString("id")
        var isFile = arguments.getBoolean("isFile")
        if (isFile)
        {
            var openDriveFileClient = OpenDriveFileApiClient(this.context)
            openDriveFileClient.trash(id)
        }
        else
        {
            var openDriveFolderClient = OpenDriveFolderApiClient(this.context)
            openDriveFolderClient.trash(id)
        }
        dialog.dismiss()
        trashDialogListener.onSuccess(dialog)
    }
*/
}