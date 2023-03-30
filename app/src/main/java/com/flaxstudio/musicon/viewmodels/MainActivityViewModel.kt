package com.flaxstudio.musicon.viewmodels

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.flaxstudio.musicon.rooms.Song
import com.flaxstudio.musicon.rooms.SongRepository
import com.flaxstudio.musicon.services.MusicPlaybackService
import com.flaxstudio.musicon.utils.FileManager
import com.flaxstudio.musicon.utils.SharedPreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class MainActivityViewModel(private val repository: SongRepository) : ViewModel() {

    val spManager = SharedPreferenceManager()             // used for access app settings data & more

    // the below both variable is used by music fragment to play music
    lateinit var selectedSong: Song
    var openedPlaylistName = ""



    // ---------------------------------------- room methods -----------------------------------
    fun getAllSongs()  = repository.getAllSongs()
    fun getAllFavouriteSongs() = repository.getAllFavouriteSongs()
    fun getAllPlaylistSongs(playlistName: String) = repository.getPlaylistSongs(playlistName)

    // limit how much recent songs you needed
    fun getAllRecentPlayedSongs(limit: Int) = repository.getRecentPlayedSongs(limit)

    fun clearAllFavouritesSong(callback: () -> Unit) = viewModelScope.launch(Dispatchers.Default) {
        repository.clearFavouriteSongs()
        withContext(Dispatchers.Main){
            callback()
        }
    }
    fun clearPlaylistSongs(playlistName: String, callback: () -> Unit) = viewModelScope.launch(Dispatchers.Default) {
        repository.clearPlaylistSongs(playlistName)
        withContext(Dispatchers.Main){
            callback()
        }
    }
    fun removeSong(song: Song, callback: () -> Unit) = viewModelScope.launch(Dispatchers.Default) {
        // remove song from table not from local
        repository.removeSong(song)
        withContext(Dispatchers.Main){
            callback()
        }
    }
    fun addSong(song: Song, callback: () -> Unit) = viewModelScope.launch(Dispatchers.Default) {
        repository.insertSong(song)
        withContext(Dispatchers.Main){
            callback()
        }
    }
    fun updateSong(song: Song, callback: () -> Unit) = viewModelScope.launch(Dispatchers.Default) {
        repository.updateSong(song)
        withContext(Dispatchers.Main){
            callback()
        }
    }






    // ------------------------------------- file system external methods ----------------------------
    private val fileManager = FileManager()

    // getting all songs file from storage and then save it to database
    fun saveAllSongsToDatabase(context: Context, callback: () -> Unit) = viewModelScope.launch(Dispatchers.Default) {
        val audioFiles = fileManager.getAllAudioFiles(context)
        val time = System.currentTimeMillis()

        for (filePath in audioFiles){
            val file = File(filePath)
            val song = Song(0,file.name, filePath, false, time, "")
            repository.insertSong(song)                 // this will add only if row not exist
        }
        withContext(Dispatchers.Main){
            callback()
        }
    }



    // ----------------------------------- common methods ------------------------------------
}




// the below class help us to take parameter in view model class as you can see above
class MainActivityViewModelFactory(private val repository: SongRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainActivityViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

