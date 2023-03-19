package com.flaxstudio.musicon.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flaxstudio.musicon.R
import com.flaxstudio.musicon.databinding.FragmentFilePickerBinding
import com.flaxstudio.musicon.databinding.FragmentHomeBinding


class FilePickerFragment : Fragment() {

    private lateinit var binding: FragmentFilePickerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFilePickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // write here...
    }
}