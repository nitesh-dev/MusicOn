package com.flaxstudio.musicon.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flaxstudio.musicon.R
import com.flaxstudio.musicon.databinding.FragmentSongsBinding

class SongsFragment : Fragment() {


    private lateinit var binding : FragmentSongsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSongsBinding.inflate(inflater, container, false)
        return  binding.root
    }


}