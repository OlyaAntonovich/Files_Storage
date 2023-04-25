package com.example.files_storage.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.files_storage.adapter.Adapter
import com.example.files_storage.data.Item
import com.example.files_storage.databinding.FragmentFirstBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.divider.MaterialDividerItemDecoration
import java.io.File


class FragmentFirst : Fragment() {
    private var _binding: FragmentFirstBinding? = null
    private val binding get() = requireNotNull(_binding)
    private var items = mutableListOf<Item>()
    private var itemsFromFile = mutableListOf<Item>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentFirstBinding.inflate(inflater, container, false)
        .also { _binding = it }
        .root


    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        FileDialogFragment().show(
//            childFragmentManager, FileDialogFragment.TAG)

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
                    fileWriteTo(items)
                    itemsFromFile = fileToTakeOut()
                    adapter.setNewList(itemsFromFile)
//                    tvText.text = itemsFromFile.size.toString()
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

    private fun fileWriteTo(list: MutableList<Item>) {

        val filename = FILE_NAME

        val listRows = mutableListOf<String>()

        var row = ""

        list.forEach {
            row = (it.name + "/" + it.surname + "/" +
                    it.phone + "/" + it.age + "/" +
                    it.dob)
            listRows.add(row)
        }

        requireContext().openFileOutput(filename, Context.MODE_PRIVATE).use {

            it.write(listRows.joinToString("\n").toByteArray())

        }

    }

    private fun fileToTakeOut(): MutableList<Item> {

        val listItems = mutableListOf<Item>()

        requireContext().openFileInput(FILE_NAME).bufferedReader().useLines { lines ->

            lines.forEach {
                val list = it.split("/") as MutableList<String>

                val item = Item(
                    list[0],
                    list[1],
                    list[2],
                    list[3],
                    list[4]
                )
                listItems.add(item)
            }
        }

        return listItems
    }

    private fun isFileExists() {

        val filePath = FILE_DIRECTORY
        val file = File(filePath)

        if (file.isFile) {
            binding.tvText.text = "File exists!!"
        } else {
            binding.tvText.text ="File doesn't exist or program doesn't have access to it"
        }
    }

    companion object {
        const val FILE_NAME = "File_Storage"
        const val FILE_DIRECTORY = "/data/data/com.example.files_storage/files/File_Storage"
    }

}