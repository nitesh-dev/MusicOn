package com.flaxstudio.musicon.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.flaxstudio.musicon.fragments.FavouritesFragment
import com.flaxstudio.musicon.fragments.PlaylistsFragment
import com.flaxstudio.musicon.fragments.SongsFragment

class PlaylistPagerAdapter (fragment: Fragment) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
       return when(position){
           0 -> SongsFragment()
           1 -> PlaylistsFragment()
           else -> FavouritesFragment()
       }
    }
}