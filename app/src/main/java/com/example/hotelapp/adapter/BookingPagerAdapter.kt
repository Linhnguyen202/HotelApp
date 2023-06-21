package com.example.hotelapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.hotelapp.ui.bookingPage.CancelScreen
import com.example.hotelapp.ui.bookingPage.CurrentScreen
import com.example.hotelapp.ui.bookingPage.PassScreen

class BookingPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return 3;
    }

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0->{
               CurrentScreen()
            }
            1->{
                PassScreen()
            }
            2->{
                CancelScreen()
            }
            else->CurrentScreen()
        }
    }

}