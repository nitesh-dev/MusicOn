package com.flaxstudio.musicon

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.flaxstudio.musicon.databinding.ActivityMainBinding
import com.flaxstudio.musicon.services.MusicPlaybackService
import com.flaxstudio.musicon.viewmodels.MainActivityViewModel
import com.flaxstudio.musicon.viewmodels.MainActivityViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val mainActivityViewModel: MainActivityViewModel by viewModels {
        MainActivityViewModelFactory((application as ProjectApplication).repository)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setTheme(R.style.Theme_blue)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // bing the service
        val intent = Intent(this, MusicPlaybackService::class.java)
        startService(intent)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)

        initialiseListeners()

    }

    private fun initialiseListeners(){
        binding.bottomMusicTrack.playPause.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) musicService!!.resumeMusic() else musicService!!.pauseMusic()
        }

    }

    // service
    private fun stopMusicService(){
        val intent = Intent(this, MusicPlaybackService::class.java)
        stopService(intent)
    }

    private var musicService: MusicPlaybackService? = null

    fun getMusicService(): MusicPlaybackService?{
        return musicService
    }

    private val connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            // Called when the connection to the service has been established
            // Get a reference to the bound service and interact with it here
            val binder: MusicPlaybackService.MyBinder = service as MusicPlaybackService.MyBinder
            musicService = binder.getService()

        }

        override fun onServiceDisconnected(name: ComponentName) {

            Log.e("----------------", "Service stopped")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopMusicService()
        musicJob?.cancel()
    }


    private var musicJob: Job? = null
    private fun initialiseData(){

        musicJob?.cancel()                  // stop previous running job
        binding.bottomMusicTrack.playPause.isChecked = musicService!!.isMusicPlaying()
        binding.bottomMusicTrack.progressBar.max = musicService!!.getMusicDuration() / 1000

        // updating seek bar on every second
        musicJob = lifecycleScope.launch(Dispatchers.Main){
            while (true){
                binding.bottomMusicTrack.progressBar.progress = musicService!!.getMusicSeek() / 1000
                delay(1000)
            }
        }
    }



    // Register broadcast receiver
    // Define broadcast receiver to sync music data changes
    private val musicChangeStateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            initialiseData()
        }
    }

    private val musicPlayPauseStateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            binding.bottomMusicTrack.playPause.isChecked = musicService!!.isMusicPlaying()
        }
    }

    override fun onResume() {
        super.onResume()
        startBroadcastReceiver()
    }

    // Unregister broadcast receiver
    override fun onPause() {
        super.onPause()
        stopBroadcastReceiver()
    }

    private fun startBroadcastReceiver(){
        val filter1 = IntentFilter()
        filter1.addAction("android.intent.action.MUSIC_STATE_CHANGE")
        registerReceiver(musicChangeStateReceiver, filter1)

        val filter2 = IntentFilter()
        filter2.addAction("android.intent.action.MUSIC_PLAY_PAUSE_STATE_CHANGE")
        registerReceiver(musicPlayPauseStateReceiver, filter2)
    }

    private fun stopBroadcastReceiver(){
        unregisterReceiver(musicChangeStateReceiver)
        unregisterReceiver(musicPlayPauseStateReceiver)
    }
}