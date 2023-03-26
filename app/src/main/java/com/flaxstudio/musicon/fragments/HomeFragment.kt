package com.flaxstudio.musicon.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.flaxstudio.musicon.ProjectApplication
import com.flaxstudio.musicon.SettingActivity
import com.flaxstudio.musicon.databinding.FragmentHomeBinding
import com.flaxstudio.musicon.viewmodels.MainActivityViewModel
import com.flaxstudio.musicon.viewmodels.MainActivityViewModelFactory


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val mainActivityViewModel: MainActivityViewModel by activityViewModels {
        MainActivityViewModelFactory((requireActivity().application as ProjectApplication).repository)
    }

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

        // write here...

        // use below code to navigate
//        findNavController().navigate(R.id.action_homeFragment_to_musicFragment)
        // findNavController().navigate(R.id.action_homeFragment_to_playlistFragment)

        binding.settingsBtn.setOnClickListener {
            val intent = Intent(activity,SettingActivity::class.java)
            startActivity(intent)
       }

    }
}