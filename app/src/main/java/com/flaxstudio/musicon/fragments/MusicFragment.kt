package com.flaxstudio.musicon.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.flaxstudio.musicon.R
import com.flaxstudio.musicon.databinding.FragmentHomeBinding
import com.flaxstudio.musicon.databinding.FragmentMusicBinding


class MusicFragment : Fragment() {

    private lateinit var binding: FragmentMusicBinding

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

        binding.playMusicView
        ViewCompat.setElevation(binding.playMusicView, 10f)
        //ViewCompat.setBackground(binding.playMusicView, ColorDrawable(Color.RED))
        binding.playMusicView.invalidate()
        // write here...
        //binding.playCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.pink_700))
        binding.playImageView.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.play_music_disc_start_anim))

        binding.musicSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                binding.playMusicView.setProgressValue(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }
}