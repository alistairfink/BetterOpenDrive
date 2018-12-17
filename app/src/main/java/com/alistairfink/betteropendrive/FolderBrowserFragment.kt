package com.alistairfink.betteropendrive

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alistairfink.betteropendrive.dataModels.FolderModel
import com.alistairfink.betteropendrive.helpers.InternalStorageClient
import kotlinx.android.synthetic.main.fragment_folder_browser.*

class FolderBrowserFragment : Fragment()
{
    companion object {
        fun newInstance(folderId: Int): FolderBrowserFragment
        {
            var fragment = FolderBrowserFragment()
            var args = Bundle()
            args.putInt("folderId", folderId)
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanteState: Bundle?): View
    {
        return inflater.inflate(R.layout.fragment_folder_browser, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var folderId = arguments.getInt("folderId").toString()
        test_text.text = folderId
    }
}