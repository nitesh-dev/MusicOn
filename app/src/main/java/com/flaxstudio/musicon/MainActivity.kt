package com.flaxstudio.musicon

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
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
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
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

            initialiseData()

            Toast.makeText(applicationContext, musicService!!.isServiceRunning().toString(), Toast.LENGTH_SHORT).show()

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
        binding.bottomMusicTrack.progressBar.max = musicService!!.getMusicDuration() / 1000

        // updating seek bar on every second
        musicJob = lifecycleScope.launch(Dispatchers.Main){
            while (true){
                binding.bottomMusicTrack.progressBar.progress = musicService!!.getMusicSeek() / 1000
                delay(1000)
            }
        }
    }
}