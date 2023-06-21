package com.example.hotelapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.hotelapp.R
import com.example.hotelapp.databinding.FragmentProfileScreenBinding
import com.example.hotelapp.model.User
import com.example.hotelapp.share.sharePreferenceUtils

class ProfileScreen : Fragment() {
    lateinit var binding : FragmentProfileScreenBinding
    lateinit var userData : User
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addData()
        addEvent()
    }

    private fun addData() {
        if(sharePreferenceUtils.isSharedPreferencesExist(requireContext(),"USER","TOKEN_VALUE") && sharePreferenceUtils.isSharedPreferencesExist(requireContext(),"USER","USER_VALUE")){
            binding.notSignView.visibility = View.GONE
            binding.mainView.visibility =View.VISIBLE
            userData = sharePreferenceUtils.getUser(requireContext()).copy()
            binding.profileName.text = userData.username
        }
        else{
            binding.notSignView.visibility = View.VISIBLE
            binding.mainView.visibility =View.GONE
        }
    }

    private fun addEvent() {
        binding.logoutButton.setOnClickListener{
            sharePreferenceUtils.removeUser(requireContext())
            findNavController().navigate(R.id.action_profileScreen_to_splashActivity)
        }
        binding.signInbutton.setOnClickListener {
            findNavController().navigate(R.id.action_profileScreen_to_mainActivity3)
        }
    }

}