package com.example.hotelapp

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.hotelapp.application.HotelApplication
import com.example.hotelapp.database.HotelDatabase
import com.example.hotelapp.databinding.ActivityMainBinding
import com.example.hotelapp.repository.AuthRepository
import com.example.hotelapp.repository.HotelRepository
import com.example.hotelapp.share.sharePreferenceUtils
import com.example.hotelapp.viewModel.AuthViewModel.AuthViewModel
import com.example.hotelapp.viewModel.AuthViewModel.AuthViewModelProviderFactory
import com.example.hotelapp.viewModel.HotelViewModel
import com.example.hotelapp.viewModel.HotelViewModelProviderFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    lateinit var dialog : BottomSheetDialog
    private val repository by lazy {
        HotelRepository(HotelDatabase(this));
    }
    private val viewModelProviderFactory by lazy {
        HotelViewModelProviderFactory(HotelApplication(),repository)
    }
    private val mainHotelView by lazy {
        ViewModelProvider(this,viewModelProviderFactory)[HotelViewModel::class.java]
    }

    public val authRepository by lazy {
        AuthRepository(HotelDatabase(this));
    }
    public val authViewModelProviderFactory by lazy {
        AuthViewModelProviderFactory(HotelApplication(),authRepository)
    }
    private val authViewModel by lazy {
        ViewModelProvider(this,authViewModelProviderFactory)[AuthViewModel::class.java]
    }

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkLocalLanguage()
        var controller =findNavController(R.id.containerFragment)
        binding.bottomNavigation.setupWithNavController(controller)

    }
    private fun checkLocalLanguage(){
        var codeLan = sharePreferenceUtils.getLanguage(this).toString()
        if(codeLan.isNullOrEmpty() || codeLan.isNullOrBlank()){
            codeLan = "eng"
            sharePreferenceUtils.saveLanguage(codeLan,this)
            setLocal(codeLan)
        }
        else{
            setLocal(codeLan)
        }

    }

    public fun setLocal(code: String){
        val locale : Locale = Locale(code);
        Locale.setDefault(locale)
        val config : Configuration = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config,baseContext.resources.displayMetrics)
    }

}