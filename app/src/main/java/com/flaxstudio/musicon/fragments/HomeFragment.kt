package com.flaxstudio.musicon.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.flaxstudio.musicon.ProjectApplication
import com.flaxstudio.musicon.R
import com.flaxstudio.musicon.SettingActivity
import com.flaxstudio.musicon.adapters.RecyclerViewSongListAdapter
import com.flaxstudio.musicon.databinding.FragmentHomeBinding
import com.flaxstudio.musicon.rooms.Song
import com.flaxstudio.musicon.viewmodels.MainActivityViewModel
import com.flaxstudio.musicon.viewmodels.MainActivityViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels {
        MainActivityViewModelFactory((requireActivity().application as ProjectApplication).repository)
    }
    private val tag = "HomeFragment.kt"

    private lateinit var contextApp: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contextApp = requireContext()

        // use below code to navigate
        // findNavController().navigate(R.id.action_homeFragment_to_musicFragment)
        // findNavController().navigate(R.id.action_homeFragment_to_playlistFragment)

        if(!isReadPermissionGranted()){
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }else{
            loadData()
        }

        setListeners()
        setupRecentRecyclerView()
        setDataObservers()
    }


    private fun setListeners(){
        binding.settingsBtn.setOnClickListener {
            val intent = Intent(activity,SettingActivity::class.java)
            startActivity(intent)
        }
    }


    private lateinit var listAdapter: RecyclerViewSongListAdapter
    private fun setupRecentRecyclerView(){

        listAdapter = RecyclerViewSongListAdapter(contextApp, lifecycleScope,object : RecyclerViewSongListAdapter.OnItemClickListener{
            override fun onItemClick(song: Song) {

                mainActivityViewModel.clearPlaylist()
                for (songData in listAdapter.currentList){
                    mainActivityViewModel.addPlaylistItem(songData)
                }

                mainActivityViewModel.selectedSong = song
                mainActivityViewModel.openedPlaylistName = "Recent"
                openSongFragment()
            }

            override fun onFavouriteClick(song: Song, isChecked: Boolean) {
                song.isFav = isChecked
                mainActivityViewModel.selectedSong = song
            }
        })
        binding.recentSongRecyclerView.adapter = listAdapter


        // disable vertically scrolling
        val layoutManager = object:LinearLayoutManager(contextApp){
            override fun canScrollVertically(): Boolean {
                return false
            }
        }

        binding.recentSongRecyclerView.layoutManager = layoutManager

    }

    private fun setDataObservers(){

        lifecycleScope.launch {
            mainActivityViewModel.getAllRecentPlayedSongs(10).collect(){songList ->
                listAdapter.submitList(songList)
            }
        }
    }

    private fun loadData(){
        mainActivityViewModel.saveAllSongsToDatabase(contextApp){
            // on data load
        }
    }




    // -----------------------------  read file permission ----------------------------------

    // Register permission launcher
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted ->
        if(isGranted){
            // do further task
            loadData()
        }else{
            // explain why you need permission
            Toast.makeText(contextApp, "Access denied", Toast.LENGTH_SHORT).show()
        }
    }

    // check permission is already granted or not
    private fun isReadPermissionGranted(): Boolean{
        if(ContextCompat.checkSelfPermission(contextApp, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }


    fun openSongFragment(){
        findNavController().navigate(R.id.action_homeFragment_to_musicFragment)
    }
}