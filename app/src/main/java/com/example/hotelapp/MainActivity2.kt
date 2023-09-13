package com.example.hotelapp

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navArgs
import com.example.hotelapp.application.HotelApplication
import com.example.hotelapp.database.HotelDatabase
import com.example.hotelapp.databinding.ActivityMain2Binding
import com.example.hotelapp.repository.AuthRepository
import com.example.hotelapp.share.sharePreferenceUtils
import com.example.hotelapp.ui.DetailScreen
import com.example.hotelapp.viewModel.AuthViewModel.AuthViewModel
import com.example.hotelapp.viewModel.AuthViewModel.AuthViewModelProviderFactory
import java.util.*

class MainActivity2 : AppCompatActivity() {
    lateinit var binding:  ActivityMain2Binding
    val args : MainActivity2Args by navArgs();
    public val authRepository by lazy {
        AuthRepository(HotelDatabase(this));
    }
    public val authViewModelProviderFactory by lazy {
        AuthViewModelProviderFactory(HotelApplication(),authRepository)
    }
    private val authViewModel by lazy {
        ViewModelProvider(this,authViewModelProviderFactory)[AuthViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
        checkLocalLanguage()
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