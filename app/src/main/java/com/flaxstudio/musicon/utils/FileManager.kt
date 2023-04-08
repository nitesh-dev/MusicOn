package com.flaxstudio.musicon.utils

import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.flaxstudio.musicon.rooms.Song
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
        MediaStore.Audio.Media.DATA,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.ALBUM_ID

    )

    // this function will return all type of audio files to implement just call this function
    // from coroutine.
    // Note: required read file permission
    fun getAllAudioFiles(context: Context): ArrayList<Song> {

        var selectionArgs: Array<String>? = null
        val audioList = ArrayList<Song>()
        val selection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // API level 29 and higher
            MediaStore.Audio.Media.IS_MUSIC + " != 0 AND " + MediaStore.Audio.Media.IS_PENDING + " = 0"
        } else {
            // Lower than API level 29
            selectionArgs = arrayOf("1")
            MediaStore.Audio.Media.IS_MUSIC + " != 0 AND " + MediaStore.Audio.Media.DATA + " IS NOT NULL"
        }


        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"
        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )
        if (cursor != null && cursor.moveToFirst()) {
            do{
                val audioPath =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))

                if(!File(audioPath).exists()){
                    continue
                }
                val title =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                val artist =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                val duration =
                    cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                val albumId =
                    cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))

                Log.e("------------", "$audioPath | $title | $artist | $duration | $albumId")
                audioList.add(Song(0, title, artist, duration, audioPath, albumId, false, 0, ""))
            } while (cursor.moveToNext())
            cursor.close()
        }

        return audioList
    }


    // TODO file system api not completed - affected (FilePickerFragment)

    private lateinit var externalStorageDir: File
    private var isFileSystemInitialised = false

    private fun initialiseFileSystem() {
        if (isFileSystemInitialised) return
        externalStorageDir = Environment.getExternalStorageDirectory()
    }

    fun listDirectory(path: String) {
        initialiseFileSystem()

        if (isFolder(path)) {
            val file = File(externalStorageDir, path)
            // return file.listFiles()
        }
    }

    fun isExist(path: String): Boolean {
        initialiseFileSystem()
        val file = File(externalStorageDir, path)
        return file.exists()
    }

    fun isFolder(path: String): Boolean {
        initialiseFileSystem()
        val file = File(externalStorageDir, path)
        return file.isDirectory
    }

    fun deleteFile(path: String) {
        initialiseFileSystem()
        if (isExist(path)) {
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

    fun saveFileInternal(context: Context, jsonFileName: String, content: String) {
        context.openFileOutput(jsonFileName, Context.MODE_PRIVATE).use {
            it.write(content.toByteArray())
            it.close()
        }
    }

    private fun getFileInternal(context: Context, jsonFileName: String): String {
        context.openFileInput(jsonFileName).use {
            return String(it.readBytes())
        }
    }
}