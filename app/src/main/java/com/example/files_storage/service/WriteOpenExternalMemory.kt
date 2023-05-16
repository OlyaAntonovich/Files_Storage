package com.example.files_storage.service

import android.content.Context
import android.os.Environment
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

class WriteOpenExternalMemory(private val context: Context) {

    private val state = Environment.getExternalStorageState()


    fun writeToExternalMemory() {

        if(state==Environment.MEDIA_MOUNTED){

            val file = File(context.getExternalFilesDir(null), FILE_EXTERNAL_NAME)

            BufferedWriter(FileWriter(file)).use {
                it.write("cjtyjtdydydtydytd")
            }
        }

    }

    companion object {

        const val FILE_EXTERNAL_NAME = "File_External"
    }
}
