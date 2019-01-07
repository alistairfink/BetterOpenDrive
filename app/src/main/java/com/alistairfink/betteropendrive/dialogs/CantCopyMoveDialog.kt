package com.alistairfink.betteropendrive.dialogs

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.TextView
import com.alistairfink.betteropendrive.R

class CantCopyMoveDialog: DialogFragment()
{
    companion object
    {
        fun newInstance(isFile: Boolean, copy: Boolean): CantCopyMoveDialog
        {
            var fragment = CantCopyMoveDialog()
            var args = Bundle()
            fragment.arguments = args
            args.putBoolean("isFile", isFile)
            args.putBoolean("copy", copy)
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.dialog_cant_copy_move, null)

        var isFile = arguments.getBoolean("isFile")
        var copy = arguments.getBoolean("copy")
        var description = view.findViewById(R.id.dialog_cant_copy_move_description) as TextView
        var text = "Cannot "
        if (copy)
        {
            text += "copy "
        }
        else
        {
            text += "move "
        }
        if (isFile)
        {

            text += "file here"
        }
        else
        {
            text += "folder here"
        }
        description.text = text

        builder
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.cancel()
                }

        builder.setView(view)
        return builder.create()
    }
}