package com.example.hotelapp.ui

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.hotelapp.MainActivity2
import com.example.hotelapp.R
import com.example.hotelapp.application.HotelApplication
import com.example.hotelapp.database.HotelDatabase
import com.example.hotelapp.databinding.FragmentDetailBookingScreenBinding
import com.example.hotelapp.model.bookingBody
import com.example.hotelapp.repository.HotelRepository
import com.example.hotelapp.share.sharePreferenceUtils
import com.example.hotelapp.utils.Resource
import com.example.hotelapp.utils.loadingDialog
import com.example.hotelapp.viewModel.UserViewModel.UserViewModel
import com.example.hotelapp.viewModel.UserViewModel.UserViewModelProviderFactory
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*


class DetailBookingScreen : Fragment() {
    lateinit var binding : FragmentDetailBookingScreenBinding
     val args : DetailBookingScreenArgs by navArgs()
    private val repository by lazy {
        HotelRepository(HotelDatabase(requireContext()));
    }
    private val viewModelProviderFactory by lazy {
        UserViewModelProviderFactory(HotelApplication(),repository)
    }

    private val userViewModel by lazy {
        ViewModelProvider(this,viewModelProviderFactory)[UserViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailBookingScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity?)!!.getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.back)
        if( (activity as AppCompatActivity?)!!.supportActionBar != null){
            (activity as AppCompatActivity?)!!.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            (activity as AppCompatActivity?)!!.supportActionBar?.title = null
        }
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        addData()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addData() {
        binding.detailHotelName.text = args.booking.hotel.name
        binding.rateNumCardTxt.text = args.booking.hotel.rate.toString()
        binding.detailHotelLocation.text = args.booking.hotel.address
        binding.roomTxt.text = args.booking.numberRoom.toString() + " room, " + args.booking.guestNumber + " guest"
        binding.roomTitle.text = args.booking.room.name
        Glide.with(this).load(args.booking.hotel.image).into(binding.banner)
        val distance = handleDistance(args.booking.startDate!!, args.booking.endDate!!)
        val currentDate = LocalDate.now()
        val dateString = currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

        val date = args.booking.createdAt!!
        val instant = Instant.parse(date)
        val zoneId = ZoneId.systemDefault()
        val formattedDate = instant.atZone(zoneId).toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        val distanceBooking = handleDistance(formattedDate,dateString)
        binding.timeInfoTxt.text = "${args.booking.startDate} - ${args.booking.startDate} (${distance.toString()})}"
        if(distanceBooking > 3){
            binding.btnCancel.isEnabled = false
            binding.btnCancel.setBackgroundColor(android.graphics.Color.parseColor("#66c50a27"))
        }
        else{
            binding.btnCancel.isEnabled = true
            binding.btnCancel.setBackgroundColor(android.graphics.Color.parseColor("#c50a27"))
        }
        binding.btnCancel.setOnClickListener{
            submitCancel()
        }
    }
    private fun submitCancel(){
        val booking = args.booking
        userViewModel.cancelUserBooking(booking.user._id, booking._id,sharePreferenceUtils.getToken(requireContext()).toString())
        userViewModel.cancelUserBookng.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    it.data?.let { BookingResponse ->
                       loadingDialog.endLoading(requireContext())
                        findNavController().popBackStack()
                    }
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {
                    loadingDialog.startLoading(requireContext())
                }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleDistance(start: String, end: String) : Int{
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val bookingStartDate  = dateFormat.parse(start)
        val bookingEndDate  = dateFormat.parse(end)
        val startDate = LocalDate.of(
            bookingStartDate.year + 1900,
            bookingStartDate.month + 1,
            bookingStartDate.date
        )
        val endDate = LocalDate.of(
            bookingEndDate.year + 1900,
            bookingEndDate.month + 1,
            bookingEndDate.date
        )
        val distance = ChronoUnit.DAYS.between(startDate, endDate)
        return distance.toInt()
    }

}