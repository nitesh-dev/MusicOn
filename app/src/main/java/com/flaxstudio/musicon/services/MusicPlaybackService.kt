package com.flaxstudio.musicon.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.flaxstudio.musicon.rooms.Song
import com.flaxstudio.musicon.utils.PlayType
import java.io.File

class MusicPlaybackService : Service() {
    private var musicPlayType = PlayType.Linear
    private val musicPlaylist = ArrayList<Song>()
    private var currentMusicPlaying: Song? = null
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

    private fun getSongIndex(song: Song): Int {
        for(index in 0 until musicPlaylist.size){
            if(song.audioPath == musicPlaylist[index].audioPath){
                return index
            }
        }
        return -1
    }







    // ---------------------------- Broadcast receiver for listening changes  --------------------------

    private fun sendMusicChangeBroadcast(){
        // Send broadcast to notify other Fragments of the button state change
        val intent = Intent()
        intent.action = "android.intent.action.MUSIC_STATE_CHANGE"
        sendBroadcast(intent)
    }

    private fun sendPlayPauseBroadcast(){
        // Send broadcast to notify other Fragments of the button play pause state change
        val intent = Intent()
        intent.action = "android.intent.action.MUSIC_PLAY_PAUSE_STATE_CHANGE"
        sendBroadcast(intent)
    }



    // ----------------------------- some useful methods ----------------------------
    /** Play new music at given path
     * @param path sting file path to play*/
    fun playNewMusic(song: Song){

        try {
            if(currentMusicPlaying != null){
                if(currentMusicPlaying!!.audioPath == song.audioPath) return          // don't play new music if same music is playing
            }
            currentMusicPlaying = song

            val uri = Uri.fromFile(File(song.audioPath))
            if(player != null && player!!.isPlaying){           // stop player if it is already playing
                player!!.stop()
                player!!.release()
            }
            player = MediaPlayer.create(this, uri)
            player!!.isLooping = false
            Log.e(tag, "--------- ${player!!.isLooping}")
            player!!.start()

            player!!.setOnCompletionListener {
                sendPlayPauseBroadcast()
            }

        }catch (ex: Exception){
            ex.printStackTrace()
            Log.e(tag, "Unable to play music")
        }

        sendMusicChangeBroadcast()
    }


    /**resume the music*/
    fun resumeMusic(){
        player?.start()
        sendPlayPauseBroadcast()
    }

    /**pause the music*/
    fun pauseMusic(){
        player?.pause()
        sendPlayPauseBroadcast()
    }

    /**check whether music is playing or not*/
    fun isMusicPlaying(): Boolean{
        if(player == null) return false
        return player!!.isPlaying
    }

    fun getPlayingMusicTitle(): String{
        if(currentMusicPlaying == null){
            return "Empty"
        }
        return currentMusicPlaying!!.audioPath
    }

    fun getPlayingMusicArtist(): String{
        if(currentMusicPlaying == null){
            return "Empty"
        }
        return currentMusicPlaying!!.artist
    }



    /** It will return music duration
     * @return music duration in millisecond*/
    fun getMusicDuration(): Int{
        return if(player == null) 0 else player!!.duration
    }

    /**Get player seek position
     * @return seek in millisecond*/
    fun getMusicSeek(): Int{
        return if(player != null) player!!.currentPosition else 0
    }


    /**Set player seek position
     * @param seekTo in millisecond*/
    fun setMusicSeek(seekTo: Int){
        if(player != null) player!!.seekTo(seekTo)
    }

    /**Set playlist array
     * @param playType It may be OneLoop | Shuffle | Linear - default value is Linear*/
    fun setMusicPlaylist(musicPaths: ArrayList<Song>){
        musicPlaylist.clear()
        musicPlaylist.addAll(musicPaths)
    }

    /**Forward 1 music & play it if possible
     * @return true - if music changed, otherwise false*/
    fun forwardMusic(): Boolean{
        if(hasForwardMusic()){
            playNewMusic(musicPlaylist[getSongIndex(currentMusicPlaying!!) + 1])
            return true
        }
        return false
    }

    /**Backward 1 music & play it if possible
     * @return true - if music changed, otherwise false*/
    fun backwardMusic(): Boolean {
        if(hasBackwardMusic()){
            playNewMusic(musicPlaylist[getSongIndex(currentMusicPlaying!!) - 1])
            return true
        }

        return false
    }

    /**check whether forward possible or not
     * @return Boolean*/
    fun hasForwardMusic(): Boolean {

        if(currentMusicPlaying == null) return false
        val playlistSize = musicPlaylist.size
        if(playlistSize > 0){
            if(getSongIndex(currentMusicPlaying!!) + 1 < playlistSize) return true
        }
        return false
    }

    /**check whether backward possible or not
     * @return Boolean*/
    fun hasBackwardMusic(): Boolean {
        if(currentMusicPlaying == null) return false
        val playlistSize = musicPlaylist.size
        if(playlistSize > 0){
            if(getSongIndex(currentMusicPlaying!!) - 1 > -1) return true
        }
        return false
    }



    /** Set player music play type
     * @param playType It may be OneLoop | Shuffle | Linear - default value is Linear*/
    fun setMusicPlayType(playType: PlayType){
        musicPlayType = playType
    }

    /** Get service status
     * @return true | false*/
    fun isServiceRunning(): Boolean {
        return isServiceRunning
    }

}