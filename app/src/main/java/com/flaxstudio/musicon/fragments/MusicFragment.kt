package com.flaxstudio.musicon.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.flaxstudio.musicon.MainActivity
import com.flaxstudio.musicon.ProjectApplication
import com.flaxstudio.musicon.R
import com.flaxstudio.musicon.databinding.FragmentMusicBinding
import com.flaxstudio.musicon.services.MusicPlaybackService
import com.flaxstudio.musicon.viewmodels.MainActivityViewModel
import com.flaxstudio.musicon.viewmodels.MainActivityViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MusicFragment : Fragment() {

    private lateinit var binding: FragmentMusicBinding

    // this variable will help not to fetch data while one touch seekbar
    private var isMusicSeekingAllowed = true
    private var musicJob: Job? = null
    private val tag = "MusicFragment.kt"



    private val mainActivityViewModel: MainActivityViewModel by activityViewModels {
        MainActivityViewModelFactory((requireActivity().application as ProjectApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMusicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //ViewCompat.setElevation(binding.playMusicView, 10f)
        //ViewCompat.setBackground(binding.playMusicView, ColorDrawable(Color.RED))
        //binding.playMusicView.invalidate()
        // write here...
        //binding.playCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.pink_700))

        initialiseListeners()

        if(musicService != null){
            musicService!!.playNewMusic(mainActivityViewModel.selectedSong.path)
            initialiseData()
        }
    }

    private fun initialiseListeners(){
        binding.musicSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                binding.playMusicView.setProgressValue(progress)
                binding.currentDuration.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                isMusicSeekingAllowed = false
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                isMusicSeekingAllowed = true
                musicService!!.setMusicSeek(seekBar.progress * 1000)
            }
        })
    }

    private fun initialiseData(){

        musicJob?.cancel()                  // stop previous running job

        binding.playImageView.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.play_music_disc_start_anim))
        binding.playlistName.text = mainActivityViewModel.openedPlaylistName
        binding.maxDuration.text = (musicService!!.getMusicDuration() / 1000).toString()
        binding.musicSeekBar.max = musicService!!.getMusicDuration() / 1000
        binding.playMusicView.setMaxProgressValue(musicService!!.getMusicDuration() / 1000)

        musicJob = lifecycleScope.launch(Dispatchers.Main){
            while (true){
                delay(1000)
                if(!isMusicSeekingAllowed) continue
                binding.musicSeekBar.progress = musicService!!.getMusicSeek() / 1000
            }
        }
    }


    private var musicService: MusicPlaybackService? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            musicService = context.getMusicService()
        }
    }
}