package com.flaxstudio.musicon.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.flaxstudio.musicon.R
import com.flaxstudio.musicon.adapters.PlaylistRecyclerViewAdapter
import com.flaxstudio.musicon.databinding.FragmentSongsBinding

class SongsFragment : Fragment() {


    private lateinit var binding : FragmentSongsBinding
    private lateinit var adapter : PlaylistRecyclerViewAdapter
    private lateinit var contextApp: Context
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contextApp = requireContext()
        adapter = PlaylistRecyclerViewAdapter()
        binding.rvSongs.layoutManager = LinearLayoutManager(contextApp)
        binding.rvSongs.adapter = adapter

    }


}