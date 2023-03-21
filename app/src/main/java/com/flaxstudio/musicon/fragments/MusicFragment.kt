package com.flaxstudio.musicon.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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

        // write here...
        //binding.playCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.pink_700))
    }
}