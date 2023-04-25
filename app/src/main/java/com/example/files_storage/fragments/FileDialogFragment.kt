package com.example.files_storage.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.files_storage.R

class FileDialogFragment: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage("Is file exist?")
            .setPositiveButton(getString(R.string.ok)) { _,_ -> }
            .create()

    companion object {
        const val TAG = "FileDialog"
    }
}