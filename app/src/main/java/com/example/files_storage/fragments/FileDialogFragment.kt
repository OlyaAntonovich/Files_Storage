package com.example.files_storage.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class FileDialogFragment: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage("File exists!")
            .setPositiveButton("save data") { _,_ ->}
            .setNegativeButton("delete file"){_, _ ->}
            .create()

    companion object {
        const val TAG = "FileDialog"
    }
}