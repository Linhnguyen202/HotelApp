package com.example.hotelapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hotelapp.R
import com.example.hotelapp.adapter.HotelMainAdapter
import com.example.hotelapp.application.HotelApplication
import com.example.hotelapp.database.HotelDatabase
import com.example.hotelapp.databinding.FragmentListHotelScreenBinding
import com.example.hotelapp.model.FavorBody
import com.example.hotelapp.model.Hotel
import com.example.hotelapp.model.HotelResponse
import com.example.hotelapp.repository.FavorRepository
import com.example.hotelapp.repository.HotelRepository
import com.example.hotelapp.share.sharePreferenceUtils
import com.example.hotelapp.utils.Resource
import com.example.hotelapp.viewModel.FavorViewModel.FavorViewModel
import com.example.hotelapp.viewModel.FavorViewModel.FavorViewModelProviderFactory
import com.example.hotelapp.viewModel.HotelViewModel
import com.example.hotelapp.viewModel.HotelViewModelProviderFactory


class ListHotelScreen : Fragment() {
    lateinit var binding : FragmentListHotelScreenBinding
    val args : ListHotelScreenArgs by navArgs()
    lateinit var adapter: HotelMainAdapter
    private val repository by lazy {
        HotelRepository(HotelDatabase(requireContext()));
    }
    private val viewModelProviderFactory by lazy {
        HotelViewModelProviderFactory(HotelApplication(),repository)
    }

    private val hotelViewModel by lazy {
        ViewModelProvider(this,viewModelProviderFactory)[HotelViewModel::class.java]
    }
    private val favorRepository by lazy {
        FavorRepository(HotelDatabase(requireContext()));
    }
    private val favorViewModelProviderFactory by lazy {
        FavorViewModelProviderFactory(HotelApplication(),favorRepository)
    }
    private val favorViewModel by lazy {
        ViewModelProvider(this,favorViewModelProviderFactory)[FavorViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
       binding = FragmentListHotelScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpItem()
        addData()
        addEvent()
    }

    private fun setUpItem() {
        adapter = HotelMainAdapter(onClickItem,onClickFavorButton,checkFavorHotel)
        binding.hotelRv.apply {
            adapter = this@ListHotelScreen.adapter
        }
    }

    private fun addEvent() {
        binding.toolbarTitle.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private val onClickItem : (Hotel)->Unit = {
        val bundle = bundleOf(
            "hotel" to it,
        )
        findNavController().navigate(R.id.action_listHotelScreen_to_mainActivity2,bundle)

    }
    private val checkFavorHotel : (Hotel) -> Boolean = {
       false
    }
    private val onClickFavorButton : (Hotel, Boolean) -> Boolean = { hotel, isCheck ->
        if(sharePreferenceUtils.isSharedPreferencesExist(requireContext(),"USER","TOKEN_VALUE") && sharePreferenceUtils.isSharedPreferencesExist(requireContext(),"USER","USER_VALUE")){
            val authen = "Bearer ${sharePreferenceUtils.getToken(requireContext())}"
            val userId = sharePreferenceUtils.getUser(requireContext())._id
            val hotelId = hotel._id
            val favorBody = FavorBody(hotelId,userId)
            if(!isCheck){
                favorViewModel.deleteFavor(userId,hotelId,authen)
                favorViewModel.statusFavorResponse.observe(viewLifecycleOwner){
                    when(it){
                        is Resource.Success -> {
                            it.data?.let { DeleteFavorResponse->
                                Toast.makeText(requireContext(),DeleteFavorResponse.toString(),Toast.LENGTH_LONG).show()
                            }
                        }
                        is Resource.Error -> {

                        }
                        is Resource.Loading -> {
                            Toast.makeText(requireContext(),"Loading data",Toast.LENGTH_LONG).show()
                        }
                    }
                }
                false
            }
            else{
                favorViewModel.makeFavor(userId,authen,favorBody)
                favorViewModel.postFavorResponse.observe(viewLifecycleOwner){
                    when(it){
                        is Resource.Success -> {
                            it.data?.let { PostFavorResponse->
                                Toast.makeText(requireContext(),"Successful",Toast.LENGTH_LONG).show()
                            }
                        }
                        is Resource.Error -> {

                        }
                        is Resource.Loading -> {
                            Toast.makeText(requireContext(),"Loading data",Toast.LENGTH_LONG).show()
                        }
                    }
                }
                true
            }

        }
        else{
            Toast.makeText(requireContext(),"Please log in ",Toast.LENGTH_LONG).show()
            false
        }

    }

    private fun addData() {
        Toast.makeText(requireContext(),"${args.type}",Toast.LENGTH_LONG).show()
        binding.toolbarTitle.titleToolbar.text = "${args.type} Hotel"
        hotelViewModel.DATA_TYPE = args.type
        lifecycleScope.launchWhenCreated {
            hotelViewModel.hotelTypePage.collect{
                adapter.submitData(it)
            }
        }
    }


}