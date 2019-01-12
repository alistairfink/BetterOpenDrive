package com.alistairfink.betteropendrive.dialogs

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.ImageView
import android.widget.TextView
import com.alistairfink.betteropendrive.R
import com.alistairfink.betteropendrive.dataModels.FileModel
import com.alistairfink.betteropendrive.helpers.DataHelper
import com.google.gson.Gson
import com.squareup.picasso.Picasso

class PropertiesFileDialog: DialogFragment()
{
    private lateinit var _file: FileModel

    companion object
    {
        fun newInstance(file: FileModel): PropertiesFileDialog
        {
            var fragment = PropertiesFileDialog()
            var args = Bundle()
            fragment.arguments = args
            args.putString("file", Gson().toJson(file))
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.dialog_properties_file, null)

        var fileString = arguments.getString("file")
        var file = Gson().fromJson(fileString, FileModel::class.java)
        _file = file

        var thumbnail = view.findViewById(R.id.file_thumbnail) as ImageView
        var name = view.findViewById(R.id.file_name) as TextView
        var size = view.findViewById(R.id.file_size) as TextView
        var version = view.findViewById(R.id.file_version) as TextView
        var downloadLink = view.findViewById(R.id.file_download_link) as TextView
        var lastModified = view.findViewById(R.id.file_last_modified) as TextView
        var fullName = view.findViewById(R.id.file_full_name) as TextView

        Picasso.with(context).load(Uri.parse(_file.Thumbnail)).into(thumbnail)
        name.text = _file.Name
        fullName.text = _file.Name
        size.text = DataHelper.dataSizeToString(_file.Size)
        version.text = if (_file.Version.isNullOrBlank()) { "n/a" } else { _file.Version }
        downloadLink.text = _file.Link
        lastModified.text = _file.DateModified.toString()

        builder
                .setPositiveButton("Close") { dialog, _ ->
                    dialog.dismiss()
                }

        builder.setView(view)
        return builder.create()
    }
}