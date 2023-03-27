package com.flaxstudio.musicon.utils

import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import java.io.File

class FileManager {

    // How to use this call?
    // 1) To use this call make sure you have the right permission for file handling
    // 2) You must have to request file permission before using this class
    //    otherwise, it will shows error

    private val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.DATA
    )

    // this function will return all type of audio files to implement just call this function
    // from coroutine.
    // Note: required read file permission
    fun getAllAudioFiles(context: Context): ArrayList<String> {

        val audioList = ArrayList<String>()
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"
        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            sortOrder
        )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val audioPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                audioList.add(audioPath)
            }
            cursor.close()
        }

        return audioList
    }


    // TODO file system api not completed - affected (FilePickerFragment)

    private lateinit var externalStorageDir: File
    private var isFileSystemInitialised = false

    private fun initialiseFileSystem(){
        if(isFileSystemInitialised) return
        externalStorageDir = Environment.getExternalStorageDirectory()
    }

    fun listDirectory(path: String){
       initialiseFileSystem()

        if(isFolder(path)){
            val file = File(externalStorageDir, path)
           // return file.listFiles()
        }
    }

    fun isExist(path: String): Boolean{
        initialiseFileSystem()
        val file = File(externalStorageDir, path)
        return file.exists()
    }

    fun isFolder(path: String): Boolean{
        initialiseFileSystem()
        val file = File(externalStorageDir, path)
        return file.isDirectory
    }

    fun deleteFile(path: String){
        initialiseFileSystem()
        if(isExist(path)){
            val file = File(externalStorageDir, path)
            file.delete()
        }
    }


    // Access the device's external storage directory

//    // Access a specific directory in external storage
//    val specificDir = File(externalStorageDir, "MyDirectory")
//
//    // Check if a file exists
//    val file = File(internalStorageDir, "example.txt")
//    if (file.exists()) {
//        // File exists, do something with it
//    }
//
//    // Create a new file
//    val newFile = File(internalStorageDir, "newfile.txt")
//    if (newFile.createNewFile()) {
//        // File created successfully, do something with it
//    }
//
//    // Access the contents of a directory
//    val directory = File(internalStorageDir, "MyDirectory")
//    if (directory.isDirectory()) {
//        val files = directory.listFiles()
//        // Loop through the files and do something with them
//        for (file in files) {
//            // Do something with the file
//        }
//    }



    // --------------------------- Internal file manager ------------------

    fun saveFileInternal(context: Context, jsonFileName: String, content: String){
        context.openFileOutput(jsonFileName, Context.MODE_PRIVATE).use {
            it.write(content.toByteArray())
            it.close()
        }
    }

    private fun getFileInternal(context: Context, jsonFileName: String): String{
        context.openFileInput(jsonFileName).use {
            return String(it.readBytes())
        }
    }
}