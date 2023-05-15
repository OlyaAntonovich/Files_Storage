package com.example.files_storage.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.files_storage.MainActivity
import com.example.files_storage.adapter.Adapter
import com.example.files_storage.data.Item
import com.example.files_storage.databinding.FragmentFirstBinding
import com.example.files_storage.service.Permissions
import com.example.files_storage.service.WriteOpenExternalMemory
import com.example.files_storage.service.WriteOpenExternalMemory.Companion.FILE_EXTERNAL_NAME
import com.example.files_storage.service.WriteOpenFile
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.divider.MaterialDividerItemDecoration
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.security.Permission


class FragmentFirst : Fragment() {
    private var _binding: FragmentFirstBinding? = null
    private val binding get() = requireNotNull(_binding)
    private var items = mutableListOf<Item>()
    private var itemsFromFile = mutableListOf<Item>()

    private val hasReadPermissions: Boolean
        get() = requireContext().hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)

    private val hasWritePermissions: Boolean
        get() = requireContext().hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentFirstBinding.inflate(inflater, container, false)
        .also { _binding = it }
        .root


    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,

                )
        )

        isFileExists()

        val adapter = Adapter(items, requireContext())

        with(binding) {

            val editList = listOf(
                editTextName,
                editTextSurname, editTextPhone, editTextAge, editTextDob
            )

            editTextDob.setOnClickListener { setDatePicker(editTextDob) }

            isButtonIsEnabled(editList, button1)

            if (button1.isEnabled
            ) {
                button1.setOnClickListener {
                    val item = makeItem(editList)
                    items.add(item)
                    WriteOpenFile(requireContext()).fileWriteTo(items)
                    itemsFromFile = WriteOpenFile(requireContext()).fileToTakeOut()
                    adapter.setNewList(itemsFromFile)
                }
            }

            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
            recyclerView.addItemDecoration(
                MaterialDividerItemDecoration(
                    requireContext(),
                    MaterialDividerItemDecoration.VERTICAL
                )
            )

            button2.setOnClickListener {

                val state = Environment.getExternalStorageState()

//                val fullpath = "/sdcard/Android/data"

                if (hasWritePermissions) {

                    if (state == Environment.MEDIA_MOUNTED) {

                        val file = File(
                            requireContext().getExternalFilesDir(null),
                            FILE_EXTERNAL_NAME
                        )

                        BufferedWriter(FileWriter(file)).use {
                            it.write("cjtyjtdydydtydytd")
                        }


                    } else {
                        binding.tvText.text = "denied"
                    }

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun makeItem(listEditText: List<EditText>): Item {


        return Item(
            listEditText[0].text.toString(),
            listEditText[1].text.toString(),
            listEditText[2].text.toString(),
            listEditText[3].text.toString(),
            listEditText[4].text.toString()
        )
    }

    private fun setDatePicker(editText: EditText) {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        datePicker.show(requireActivity().supportFragmentManager, "tag")
        datePicker.addOnPositiveButtonClickListener {
            editText.setText(datePicker.headerText)
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun isButtonIsEnabled(edidList: List<EditText>, button: Button) {
        val textWatcherEditText: TextWatcher = object : TextWatcher {

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }


            @SuppressLint("ResourceAsColor")
            override fun afterTextChanged(editable: Editable?) {

                val condition: Boolean =
                    edidList.all {
                        it.text.toString().isNotEmpty()
                    }

                button.isEnabled = condition
                if (button.isEnabled) {
                    button.setBackgroundColor(
                        Color.parseColor(
                            "#0277BD"
                        )
                    )
                } else {
                    button.setBackgroundColor(
                        Color.parseColor(
                            "#E1F5FE"
                        )
                    )
                }
            }
        }

        for (element in edidList) {
            element.addTextChangedListener(textWatcherEditText)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun isFileExists() {

        val filePath = FILE_DIRECTORY
        val file = File(filePath)

        if (file.isFile) {
            FileDialogFragment().show(
                childFragmentManager, FileDialogFragment.TAG
            )
        } else {
//            binding.tvText.text = "File doesn't exist or program doesn't have access to it"
        }
    }

    companion object {
        const val FILE_NAME = "File_Storage"

        const val PERMISSION_STORAGE = 101

        @SuppressLint("SdCardPath")
        const val FILE_DIRECTORY = "/data/data/com.example.files_storage/files/File_Storage"
    }


}

fun Context.hasPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

