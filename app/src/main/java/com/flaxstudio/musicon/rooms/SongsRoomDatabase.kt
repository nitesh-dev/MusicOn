package com.flaxstudio.musicon.rooms

import android.content.Context
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Entity(tableName = "songs_table")
data class Song(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "path") var path: String,
    @ColumnInfo(name = "is_fav") var isFav: Boolean,
    @ColumnInfo(name = "last_played") var lastPlayed: Long,
    @ColumnInfo(name = "playlist") var playlist: String
)

@Dao
interface SongDao{
    @Query("SELECT * FROM songs_table")
    fun getAllSongs(): Flow<List<Song>>

    @Query("SELECT * FROM songs_table WHERE is_fav = true")
    fun getAllFavouriteSongs(): Flow<List<Song>>

    @Query("SELECT * FROM songs_table WHERE playlist = :playlistName")
    fun getPlaylistSongs(playlistName: String): Flow<List<Song>>

    @Query("UPDATE songs_table SET playlist = '' WHERE playlist = :playlistName")
    fun clearPlaylistSongs(playlistName: String)

    @Query("UPDATE songs_table SET is_fav = 0 WHERE is_fav = 1")
    fun clearFavouritesSongs()

    @Query("SELECT * FROM songs_table ORDER BY last_played DESC LIMIT :limit")
    fun getRecentSongs(limit: Int) : Flow<List<Song>>

    @Query("SELECT COUNT(id) FROM songs_table")
    fun rowCount(): Int

    @Query("SELECT COUNT(id) FROM songs_table WHERE path = :path")
    fun isSongExist(path: String): Int

    @Update
    fun updateSong(song: Song)

    @Delete
    fun removeSong(song: Song)

    @Insert
    fun addSong(song: Song)
}

class SongRepository(private val songDao: SongDao){


    fun getAllSongs(): Flow<List<Song>> {
        return songDao.getAllSongs()
    }

    fun getAllFavouriteSongs(): Flow<List<Song>> {
        return songDao.getAllFavouriteSongs()
    }

    fun getPlaylistSongs(playlistName: String): Flow<List<Song>> {
        return songDao.getPlaylistSongs(playlistName)
    }

    fun getRecentPlayedSongs(limit: Int): Flow<List<Song>> {
        return songDao.getRecentSongs(limit)
    }

    @WorkerThread
    suspend fun isTableEmpty(): Boolean{
        val count = songDao.rowCount()
        return count == 0
    }

    @WorkerThread
    suspend fun updateSong(song: Song){
        songDao.updateSong(song)
    }

    @WorkerThread
    suspend fun clearPlaylistSongs(playlistName: String){
        songDao.clearPlaylistSongs(playlistName)
    }

    @WorkerThread
    suspend fun clearFavouriteSongs(){
        songDao.clearFavouritesSongs()
    }

    @WorkerThread
    suspend fun insertSong(song: Song){
        if(songDao.isSongExist(song.path) == 0){
            songDao.addSong(song)
        }else{
            Log.e("SQL", "skipped - insert")
        }
    }

    @WorkerThread
    suspend fun removeSong(song: Song){
        songDao.removeSong(song)
    }

}

@Database(entities = [Song::class], version = 1, exportSchema = false)
abstract class AppSongDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppSongDatabase? = null

        fun getDatabase(context: Context): AppSongDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppSongDatabase::class.java,
                    "database-app"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}