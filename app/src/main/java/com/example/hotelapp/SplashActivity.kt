package com.example.hotelapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.hotelapp.share.sharePreferenceUtils

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler(Looper.myLooper()!!).postDelayed({
            if(sharePreferenceUtils.isSharedPreferencesExist(this,"USER","TOKEN_VALUE") && sharePreferenceUtils.isSharedPreferencesExist(this,"USER","USER_VALUE")){
                startActivity(Intent(this,MainActivity::class.java))
            }
            else{
                startActivity(Intent(this,MainActivity3::class.java))
            }
        },1000)
    }
}