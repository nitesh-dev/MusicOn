package com.flaxstudio.musicon.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.flaxstudio.musicon.utils.PlayType
import java.io.File

class MusicPlaybackService : Service() {
    private var musicPlayType = PlayType.Linear
    private val musicPlaylistPath = ArrayList<String>()
    private var currentMusicPlayingPath = ""
    private var player: MediaPlayer? = null
    private var isServiceRunning = false

    private val tag = "MusicPlaybackService.kt"

    // execution of service will start on calling this method
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        isServiceRunning = true
        return START_STICKY
    }

    // execution of the service will stop on calling this method
    override fun onDestroy() {
        super.onDestroy()

        // stopping the process
        player?.stop()
        isServiceRunning = false

    }


    // the below code will bind service with activity
    private val binder = MyBinder()

    inner class MyBinder : Binder() {
        fun getService(): MusicPlaybackService = this@MusicPlaybackService
    }
    override fun onBind(intent: Intent): IBinder {
        return binder
    }







    // ----------------------------- some useful methods ----------------------------
    /**
     * Play new music at given path
     * @param path sting file path to play
     */
    fun playNewMusic(path: String){

        try {
            if(currentMusicPlayingPath == path) return          // don't play new music if same music is playing

            val uri = Uri.fromFile(File(path))
            if(player != null && player!!.isPlaying){           // stop player if it is already playing
                player!!.stop()
                player!!.release()
            }
            player = MediaPlayer.create(this, uri)
            player!!.isLooping = false
            player!!.start()

        }catch (ex: Exception){
            ex.printStackTrace()
            Log.e(tag, "Unable to play music")
        }
    }

    fun getMusicDuration(): Int{
        return if(player == null) 0 else player!!.duration
    }

    fun getMusicSeek(): Int{
        return if(player != null) player!!.currentPosition else 0
    }


    fun setMusicSeek(seekTo: Int){
        if(player != null) player!!.seekTo(seekTo)
    }

    /**
     * resume the music
     */
    fun resumeMusic(){
        player?.start()
    }

    /**
     * pause the music
     */
    fun pauseMusic(){
        player?.pause()
    }


    /**
     * Set player seek position
     * @param value must be between 0 and 1 inclusive
     */
    fun setMusicSeekProgress(value: Float){
        if(player == null) return
        val seekValue = (player!!.duration * value).toInt()
        player!!.seekTo(seekValue)
    }

    /**
     * Set playlist array t
     * @param playType It may be OneLoop | Shuffle | Linear - default value is Linear
     */
    fun setMusicPlaylist(musicPaths: ArrayList<String>){
        musicPlaylistPath.clear()
        musicPlaylistPath.addAll(musicPaths)
    }

    /**
     * Set player music play type
     * @param playType It may be OneLoop | Shuffle | Linear - default value is Linear
     */
    fun setMusicPlayType(playType: PlayType){
        musicPlayType = playType
    }

    /**
     * Get service status
     * @return true | false
     */
    fun isServiceRunning(): Boolean {
        return isServiceRunning
    }

}