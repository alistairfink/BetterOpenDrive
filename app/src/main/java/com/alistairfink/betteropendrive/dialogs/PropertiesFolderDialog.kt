package com.alistairfink.betteropendrive.dialogs

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.ImageView
import android.widget.TextView
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

        var name = view.findViewById(R.id.folder_name) as TextView
        var fullName = view.findViewById(R.id.folder_full_name) as TextView
        var subFolders = view.findViewById(R.id.folder_sub_folders) as TextView
        var downloadLink = view.findViewById(R.id.folder_download_link) as TextView
        var createdDate = view.findViewById(R.id.folder_creation) as TextView
        var lastModified = view.findViewById(R.id.folder_last_modified) as TextView

        name.text = _folder.Name
        fullName.text = _folder.Name
        subFolders.text = _folder.ChildFolders.toString()
        downloadLink.text = _folder.Link
        createdDate.text = _folder.DateCreated.toString()
        lastModified.text = _folder.DateModified.toString()

        builder
                .setPositiveButton("Close") { dialog, _ ->
                    dialog.dismiss()
                }


        builder.setView(view)
        return builder.create()
    }
}