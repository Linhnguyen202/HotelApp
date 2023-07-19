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
import com.example.hotelapp.adapter.UserBookingAdapter
import com.example.hotelapp.application.HotelApplication
import com.example.hotelapp.database.HotelDatabase
import com.example.hotelapp.databinding.FragmentCancelScreenBinding
import com.example.hotelapp.model.Booking
import com.example.hotelapp.repository.HotelRepository
import com.example.hotelapp.share.sharePreferenceUtils
import com.example.hotelapp.utils.Resource
import com.example.hotelapp.viewModel.UserViewModel.UserViewModel
import com.example.hotelapp.viewModel.UserViewModel.UserViewModelProviderFactory

class CancelScreen : Fragment() {
    lateinit var binding : FragmentCancelScreenBinding
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
        binding = FragmentCancelScreenBinding.inflate(layoutInflater)
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
            adapter = this@CancelScreen.adapter
        }
    }
    private fun addData() {
        val authen = "Bearer ${sharePreferenceUtils.getToken(requireContext())}"
        val userId = sharePreferenceUtils.getUser(requireContext())._id
        userViewModel.getUserCancelBookingList(userId,authen)
        userViewModel.userBookingList.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    it.data?.let { UserBookingResponse ->
                        adapter.differ.submitList(UserBookingResponse.booking.toList())
                        binding.currentBookingRv.visibility = View.VISIBLE
                        binding.emptyLayout.visibility = View.INVISIBLE
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

    }


}