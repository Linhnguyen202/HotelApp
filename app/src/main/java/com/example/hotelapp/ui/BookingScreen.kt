package com.example.hotelapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.hotelapp.R
import com.example.hotelapp.adapter.BookingPagerAdapter
import com.example.hotelapp.databinding.FragmentBookingScreenBinding
import com.example.hotelapp.share.sharePreferenceUtils
import com.google.android.material.tabs.TabLayoutMediator


class BookingScreen : Fragment() {
    lateinit var binding : FragmentBookingScreenBinding
    lateinit var viewAdapter : BookingPagerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
       binding = FragmentBookingScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpItem()
        addData()
        addEvent()

    }

    private fun addEvent() {
        binding.signInbutton.setOnClickListener {
            findNavController().navigate(R.id.action_bookingScreen_to_mainActivity3)
        }
    }

    private fun addData() {
        if(sharePreferenceUtils.isSharedPreferencesExist(requireContext(),"USER","TOKEN_VALUE") && sharePreferenceUtils.isSharedPreferencesExist(requireContext(),"USER","USER_VALUE")){
            binding.notSignView.visibility = View.GONE
            binding.mainView.visibility =View.VISIBLE
        }
        else{
            binding.notSignView.visibility = View.VISIBLE
            binding.mainView.visibility =View.GONE
        }
    }


    private fun setUpItem() {
        viewAdapter = BookingPagerAdapter(childFragmentManager,lifecycle)
        binding.viewPageBooking.apply {
            adapter = this@BookingScreen.viewAdapter
        }
        TabLayoutMediator(binding.tabTrip,binding.viewPageBooking){tab,position->
            when(position){
                0->{
                    tab.text = getString(R.string.current)
                }
                1->{
                    tab.text = getString(R.string.pass)
                }
                2->{
                    tab.text = getString(R.string.cancel)
                }
            }
        }.attach()
    }

}