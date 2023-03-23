package com.flaxstudio.musicon.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.flaxstudio.musicon.adapters.PlaylistPagerAdapter
import com.flaxstudio.musicon.databinding.FragmentPlaylistBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class PlaylistFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistBinding

    private val tabTitles = arrayListOf("Songs" , "Playlists" , "Favourites")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // write here...
        setupLayoutWithViewPager()
    }

    private fun setupLayoutWithViewPager() {
        binding.viewPager.adapter = PlaylistPagerAdapter(this)

        TabLayoutMediator(binding.tabBarLayout , binding.viewPager){ tab , position ->
            tab.text = tabTitles[position]

        }.attach()
    }

}