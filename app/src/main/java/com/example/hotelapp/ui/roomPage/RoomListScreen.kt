package com.example.hotelapp.ui.roomPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hotelapp.R
import com.example.hotelapp.adapter.RoomAdapter
import com.example.hotelapp.application.HotelApplication
import com.example.hotelapp.database.HotelDatabase
import com.example.hotelapp.databinding.FragmentRoomListScreenBinding
import com.example.hotelapp.model.Room
import com.example.hotelapp.model.RoomResponse
import com.example.hotelapp.repository.HotelRepository
import com.example.hotelapp.utils.Resource
import com.example.hotelapp.viewModel.HotelViewModel
import com.example.hotelapp.viewModel.HotelViewModelProviderFactory
import com.example.hotelapp.viewModel.RoomViewModel.RoomViewModel
import com.example.hotelapp.viewModel.RoomViewModel.RoomViewModelProviderFactory


class RoomListScreen : Fragment() {
    lateinit var binding : FragmentRoomListScreenBinding
    lateinit var adapter: RoomAdapter
    val args : RoomListScreenArgs by navArgs();
    private val repository by lazy {
        HotelRepository(HotelDatabase(requireContext()));
    }
    private val viewModelProviderFactory by lazy {
        RoomViewModelProviderFactory(HotelApplication(),repository)
    }
    private val roomViewModel by lazy {
        ViewModelProvider(this,viewModelProviderFactory)[RoomViewModel::class.java]
    }
    var roomList : MutableList<Room> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
       binding = FragmentRoomListScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = RoomAdapter(onClick)
        binding.roomRv.apply {
            adapter = this@RoomListScreen.adapter
        }
        addData()
        addEvents()
    }

    private fun addEvents() {
        binding.toolbarTitle.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun addData() {
        binding.toolbarTitle.titleToolbar.text = args.name
        roomViewModel.setHotelId(args.id)
        roomViewModel.roomList.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    it.data.let { RoomResponse ->
                      adapter.differ.submitList(RoomResponse?.result?.toList())
                    }
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {
                    Toast.makeText(requireContext(),"Loading room",Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
    private val onClick : (Room)->Unit = {
        val bundle = bundleOf(
            "room" to it
        )
        findNavController().navigate(R.id.action_roomListScreen_to_confirmPayScreen,bundle)
    }

}