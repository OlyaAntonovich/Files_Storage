package com.example.files_storage.service

import android.content.Context
import com.example.files_storage.data.Item
import com.example.files_storage.fragments.FragmentFirst

class WriteOpenFile(private val context: Context) {

     fun fileWriteTo(list: MutableList<Item>) {
        val filename = FragmentFirst.FILE_NAME

        val listRows = mutableListOf<String>()

        var row = ""

        list.forEach {
            row = (it.name + "/" + it.surname + "/" +
                    it.phone + "/" + it.age + "/" +
                    it.dob)
            listRows.add(row)
        }

        context.openFileOutput(filename, Context.MODE_PRIVATE).use {

            it.write(listRows.joinToString("\n").toByteArray())

        }
    }

     fun fileToTakeOut(): MutableList<Item> {
        val listItems = mutableListOf<Item>()

        context.openFileInput(FragmentFirst.FILE_NAME).bufferedReader().useLines { lines ->

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
}