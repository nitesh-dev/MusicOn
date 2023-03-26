package com.flaxstudio.musicon

import android.app.Application
import com.flaxstudio.musicon.rooms.AppSongDatabase
import com.flaxstudio.musicon.rooms.SongRepository

class ProjectApplication : Application() {
    // No need to cancel this scope as it'll be torn down with the process

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    private val database by lazy { AppSongDatabase.getDatabase(this) }
    val repository by lazy { SongRepository(database.songDao()) }
}