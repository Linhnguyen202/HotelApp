package com.example.hotelapp

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import com.example.hotelapp.databinding.ActivityMain3Binding
import com.example.hotelapp.databinding.ActivityMainBinding
import com.example.hotelapp.share.sharePreferenceUtils
import java.util.*

class MainActivity3 : AppCompatActivity() {
    lateinit var binding : ActivityMain3Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain3Binding.inflate(layoutInflater)
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