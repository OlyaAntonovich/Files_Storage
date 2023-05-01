package com.example.files_storage.fragments

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.files_storage.service.WriteOpenFile
import java.io.File

class FileDialogFragment : DialogFragment() {

    private val filePath = FragmentFirst.FILE_DIRECTORY
    private val file = File(filePath)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage("File exists!")
            .setPositiveButton("save data") { _, _ ->
                Toast.makeText(
                    activity,
                    WriteOpenFile(requireContext()).fileToTakeOut().toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
            .setNegativeButton("delete file") { _, _ ->
                val result = file.delete()
                if (result) {
                    Toast.makeText(
                        activity,
                        "Deletion succeeded",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        activity,
                        "Deletion failed",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            .create()

    companion object {
        const val TAG = "FileDialog"
    }
}