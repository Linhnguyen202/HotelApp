package com.example.hotelapp.ui.bookingPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hotelapp.R
import com.example.hotelapp.adapter.BookingAdapter
import com.example.hotelapp.adapter.UserBookingAdapter
import com.example.hotelapp.application.HotelApplication
import com.example.hotelapp.database.HotelDatabase
import com.example.hotelapp.databinding.FragmentCurrentScreenBinding
import com.example.hotelapp.model.Booking
import com.example.hotelapp.model.Hotel
import com.example.hotelapp.model.UserBookingResponse
import com.example.hotelapp.repository.HotelRepository
import com.example.hotelapp.share.sharePreferenceUtils
import com.example.hotelapp.utils.Resource
import com.example.hotelapp.viewModel.HotelViewModel
import com.example.hotelapp.viewModel.HotelViewModelProviderFactory
import com.example.hotelapp.viewModel.UserViewModel.UserViewModel
import com.example.hotelapp.viewModel.UserViewModel.UserViewModelProviderFactory


class CurrentScreen : Fragment() {
    lateinit var binding : FragmentCurrentScreenBinding
    private val repository by lazy {
        HotelRepository(HotelDatabase(requireContext()));
    }
    private val viewModelProviderFactory by lazy {
        UserViewModelProviderFactory(HotelApplication(),repository)
    }

    private val userViewModel by lazy {
        ViewModelProvider(this,viewModelProviderFactory)[UserViewModel::class.java]
    }
    lateinit var adapter: UserBookingAdapter
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
        setUpItem()
        if(sharePreferenceUtils.isSharedPreferencesExist(requireContext(),"USER","TOKEN_VALUE") && sharePreferenceUtils.isSharedPreferencesExist(requireContext(),"USER","USER_VALUE")){
            addData()
        }
        else{

        }

    }

    private fun setUpItem() {
        adapter = UserBookingAdapter(onClickItem)
        binding.currentBookingRv.apply {
            adapter = this@CurrentScreen.adapter
        }
    }

    private fun addData() {
        val authen = "Bearer ${sharePreferenceUtils.getToken(requireContext())}"
        val userId = sharePreferenceUtils.getUser(requireContext())._id
        userViewModel.getUserBookingList(userId,authen)
        userViewModel.userBookingList.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    it.data?.let { UserBookingResponse ->
                        adapter.differ.submitList(UserBookingResponse.booking.toList())
                    }
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {
                    Toast.makeText(requireContext(),"Loading data", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    private val onClickItem : (Booking)->Unit = {
        val bundle = bundleOf(
           "booking" to it
        )
        findNavController().navigate(R.id.action_bookingScreen_to_detailBookingScreen,bundle)
    }
}