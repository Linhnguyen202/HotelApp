package com.example.hotelapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hotelapp.databinding.ActivityMain3Binding
import com.example.hotelapp.databinding.ActivityMainBinding

class MainActivity3 : AppCompatActivity() {
    lateinit var binding : ActivityMain3Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}