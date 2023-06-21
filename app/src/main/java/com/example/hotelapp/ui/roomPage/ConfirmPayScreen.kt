package com.example.hotelapp.ui.roomPage

import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.compose.ui.graphics.Paint
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hotelapp.MainActivity2
import com.example.hotelapp.R
import com.example.hotelapp.application.HotelApplication
import com.example.hotelapp.database.HotelDatabase
import com.example.hotelapp.databinding.FragmentConfirmPayScreenBinding
import com.example.hotelapp.model.BookingResponse
import com.example.hotelapp.model.User
import com.example.hotelapp.model.bookingBody
import com.example.hotelapp.repository.HotelRepository
import com.example.hotelapp.share.sharePreferenceUtils
import com.example.hotelapp.utils.Resource
import com.example.hotelapp.viewModel.HotelViewModel
import com.example.hotelapp.viewModel.HotelViewModelProviderFactory
import com.example.hotelapp.viewModel.UserViewModel.UserViewModel
import com.example.hotelapp.viewModel.UserViewModel.UserViewModelProviderFactory
import okhttp3.internal.assertThreadHoldsLock

class ConfirmPayScreen : Fragment() {
    lateinit var binding : FragmentConfirmPayScreenBinding
    val args : ConfirmPayScreenArgs by navArgs()
    private val repository by lazy {
        HotelRepository(HotelDatabase(requireContext()));
    }
    private val viewModelProviderFactory by lazy {
        UserViewModelProviderFactory(HotelApplication(),repository)
    }
    private val userViewModel by lazy {
        ViewModelProvider(this,viewModelProviderFactory)[UserViewModel::class.java]
    }
//    val bookingBody = bookingBody(user.username,2000,1000,"Current","",2,2,"Pay when you stay",user._id,args.room.hotel.toString(),args.room._id.toString())
    var bookingBody : bookingBody? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentConfirmPayScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addData()
        addEvents()
    }

    private fun addData() {
        val search = (activity as MainActivity2).args.search
        val argRoom = args.room
        Log.d("search",search.adults.toString())
        val user = sharePreferenceUtils.getUser(requireContext())
        val userName = user.username
        val price = argRoom.price
        val totalPrice = search.room!!.toInt() * price
        val status = "Current"
        val descripion = ""
        val numberRoom = search.room.toString().toInt()
        val guestNumber = search.adults.toString().toInt()
        val userId = user._id
        val hotelId = argRoom.hotel.toString()
        val roomId = argRoom._id
        var paymentType : String = ""
        bookingBody = bookingBody(userName!!,totalPrice,price,status,descripion,numberRoom,guestNumber,paymentType,userId,hotelId,roomId)
    }
    private fun onSubmit(){
        if(bookingBody!!.paymentType.isEmpty()){
            Toast.makeText(requireContext(),"Please choose your payment type",Toast.LENGTH_LONG).show()
        }
        else{
            Toast.makeText(requireContext(),this@ConfirmPayScreen.bookingBody.toString(),Toast.LENGTH_LONG).show()
            var header : String = "Bearer "
            val user : User = sharePreferenceUtils.getUser(requireContext())
            val token = sharePreferenceUtils.getToken(requireContext())
            header += token
            userViewModel.makeUserBooking(user._id,header, this@ConfirmPayScreen.bookingBody!!)
            userViewModel.userBooking.observe(viewLifecycleOwner){
                when(it){
                    is Resource.Success -> {
                        it.data.let { BookingResponse ->
                            val bundle = bundleOf(
                                "booking" to BookingResponse!!.booking
                            )
                         findNavController().navigate(R.id.action_confirmPayScreen_to_successOrderScreen2,bundle)
                        }
                    }
                    is Resource.Error -> {

                    }
                    is Resource.Loading -> {
                        Toast.makeText(requireContext(),"Loading",Toast.LENGTH_LONG).show()
                    }
                }
            }
        }


    }
    private fun addEvents() {
        binding.paymentContainer.setOnCheckedChangeListener { _, checkedId ->
            val selectedRadioButton = binding.paymentContainer.findViewById<RadioButton>(checkedId)
            bookingBody!!.paymentType = selectedRadioButton.text.toString()
        }

        binding.toolbar.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSubmitBook.setOnClickListener{
            onSubmit()
        }
    }

}