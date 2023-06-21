package com.example.hotelapp.ui.bookingPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hotelapp.adapter.BookingAdapter
import com.example.hotelapp.databinding.FragmentCurrentScreenBinding


class CurrentScreen : Fragment() {
    lateinit var binding : FragmentCurrentScreenBinding
//    private val adapter by lazy {
//        BookingAdapter()
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCurrentScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addData()
    }

    private fun addData() {
//        binding.currentBookingRv.apply {
//            adapter = this@CurrentScreen.adapter
//        }
//
//        adapter.differ.submitList(bookingList)
    }

}