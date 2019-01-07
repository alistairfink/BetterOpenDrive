package com.alistairfink.betteropendrive.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.TextView
import com.alistairfink.betteropendrive.R

class ConfirmOverwriteDialog: DialogFragment()
{
    private lateinit var confirmOverwriteDialogListener: IDialogListener

    companion object
    {
        fun newInstance(itemName: String, isFile: Boolean): ConfirmOverwriteDialog
        {
            var fragment = ConfirmOverwriteDialog()
            var args = Bundle()
            fragment.arguments = args
            args.putString("itemName", itemName)
            return fragment
        }
    }

    fun setConfirmOverwriteDialogListener(listener: IDialogListener)
    {
        confirmOverwriteDialogListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.dialog_overwrite, null)

        val title = view.findViewById(R.id.dialog_overwrite_title) as TextView
        title.text = "Overwrite File?"

        var itemName = arguments.getString("itemName")
        var description = view.findViewById(R.id.dialog_overwrite_description) as TextView
        description.text = "Are you sure you want to overwrite $itemName"

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
        dialog.dismiss()
        confirmOverwriteDialogListener.onSuccess(dialog)
    }
}