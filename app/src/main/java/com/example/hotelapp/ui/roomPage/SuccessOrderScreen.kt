package com.example.hotelapp.ui.roomPage

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.hotelapp.R
import com.example.hotelapp.databinding.FragmentSuccessOrderScreenBinding

class SuccessOrderScreen : Fragment() {
   lateinit var binding : FragmentSuccessOrderScreenBinding
   val args: SuccessOrderScreenArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSuccessOrderScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addEvents()
        addData()
    }

    @SuppressLint("SetTextI18n")
    private fun addData() {
        val booking = args.booking
        binding.confirmTxt.text = "A confirmation email has been sent to ${booking.user.email}"
        binding.phoneNumber.text = booking.user.phoneNumber.toString()
        binding.hotelName.text = booking.hotel.name.toString()
        binding.roomName.text = booking.room.name.toString()
        binding.paymentType.text = booking.paymentType.toString()
        binding.roomNumberInfo.text = booking.numberRoom.toString() + " room, " + booking.guestNumber.toString() + " guest"
    }

    private fun addEvents() {
        binding.doneBtn.setOnClickListener{
            activity?.finish()
        }
    }


}