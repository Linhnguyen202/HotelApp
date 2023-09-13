package com.example.hotelapp.ui.ProfilePage

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import com.example.hotelapp.R
import com.example.hotelapp.databinding.FragmentListSettingScreenBinding
import com.example.hotelapp.share.sharePreferenceUtils
import kotlin.properties.Delegates


class ListSettingScreen : Fragment() {

    lateinit var binding : FragmentListSettingScreenBinding
    lateinit var sharedPreferences : SharedPreferences.Editor
    var nightMode : Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListSettingScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarSetting.titleToolbar.text = getString(R.string.Setting)
        binding.toolbarSetting.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.lanContainer.setOnClickListener {
            findNavController().navigate(R.id.action_listSettingScreen_to_languageSettingScreen)
        }
        binding.switchbtn.setOnClickListener{
            if(nightMode){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                nightMode = false
            }
            else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                nightMode = true
            }

        }
    }
    private fun checkDarkMode(){
        val share : SharedPreferences = requireContext().getSharedPreferences("MODE",Context.MODE_PRIVATE)
        nightMode = share.getBoolean("night",false)
        binding.switchbtn.isChecked = nightMode

    }


}