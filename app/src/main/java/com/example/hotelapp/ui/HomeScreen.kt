package com.example.hotelapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hotelapp.MainActivity
import com.example.hotelapp.R
import com.example.hotelapp.adapter.CityAdapter
import com.example.hotelapp.adapter.HotelBigAdapter
import com.example.hotelapp.adapter.PopularHomeAdapter
import com.example.hotelapp.application.HotelApplication
import com.example.hotelapp.database.HotelDatabase
import com.example.hotelapp.databinding.FragmentHomeScreenBinding
import com.example.hotelapp.model.*
import com.example.hotelapp.repository.AuthRepository
import com.example.hotelapp.repository.FavorRepository
import com.example.hotelapp.repository.HotelRepository
import com.example.hotelapp.share.sharePreferenceUtils
import com.example.hotelapp.ui.start.LogInScreen
import com.example.hotelapp.utils.Resource
import com.example.hotelapp.utils.loadingDialog
import com.example.hotelapp.viewModel.AuthViewModel.AuthViewModel
import com.example.hotelapp.viewModel.AuthViewModel.AuthViewModelProviderFactory
import com.example.hotelapp.viewModel.FavorViewModel.FavorViewModel
import com.example.hotelapp.viewModel.FavorViewModel.FavorViewModelProviderFactory
import com.example.hotelapp.viewModel.HotelViewModel
import com.example.hotelapp.viewModel.HotelViewModelProviderFactory


class HomeScreen : Fragment() {
    lateinit var binding : FragmentHomeScreenBinding
    lateinit var adapterBig: HotelBigAdapter
    lateinit var adapterPop : PopularHomeAdapter
    lateinit var adapterCityAdapter: CityAdapter
    var search : Search = Search("","","","","")
    private val repository by lazy {
        HotelRepository(HotelDatabase(requireContext()));
    }
    private val viewModelProviderFactory by lazy {
        HotelViewModelProviderFactory(HotelApplication(),repository)
    }

    private val hotelViewModel by lazy {
        ViewModelProvider(this,viewModelProviderFactory)[HotelViewModel::class.java]
    }

    private val authViewModel by lazy {
        ViewModelProvider(requireActivity(),(activity as MainActivity).authViewModelProviderFactory)[AuthViewModel::class.java]
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

    private val cityList : MutableList<City> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
       binding = FragmentHomeScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpItem()
        addData()
        addEvents()

    }

    private fun addEvents() {
        binding.bookBtn.setOnClickListener {
            if(sharePreferenceUtils.isSharedPreferencesExist(requireContext(),"USER","TOKEN_VALUE") && sharePreferenceUtils.isSharedPreferencesExist(requireContext(),"USER","USER_VALUE")){

            }
            else{
            }

        }
    }



    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        val preferences = context?.getSharedPreferences("USER", Context.MODE_PRIVATE)
        if (preferences != null) {
            if (preferences.contains("USER_VALUE")) {
                binding.helloTitle.text = "Hello, " + sharePreferenceUtils.getUser(requireContext()).username
            } else {
                binding.helloTitle.text = "welcome"
            }
        }
    }

    private fun addData() {
        // add trending hotel
        hotelViewModel.setDataType("New")
        hotelViewModel.popularHotelList.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    it.data?.let { HotelResponse ->
                        adapterBig.differ.submitList(HotelResponse.result.toList())

                    }
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
            }
        }
        //get popular hotel
        hotelViewModel.setDataType("Popular")
        hotelViewModel.popularHotelList.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    it.data?.let { HotelResponse ->
                        adapterPop.differ.submitList(HotelResponse.result.toList())
                        loadingDialog.endLoading(requireContext())
                    }
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {
                   loadingDialog.startLoading(requireContext())
                }
            }
        }
        // add city
        cityList.add(City("1","HaNoi"))
        cityList.add(City("2","Ho Chi Minh City"))
        cityList.add(City("3","Phu Quoc"))
        cityList.add(City("4","Ha Long"))
        Log.d("city",cityList.toString())
        adapterCityAdapter.differ.submitList(cityList)
    }

    private fun setUpItem() {
        adapterBig = HotelBigAdapter(onClickItem,onClickFavorButton,onClickRemoveFavorButton)
        adapterPop = PopularHomeAdapter(onClickItem,onClickFavorButton,onClickRemoveFavorButton)
        adapterCityAdapter = CityAdapter()
        binding.trendingRv.apply {
            adapter = this@HomeScreen.adapterBig
        }
        binding.popularRv.apply {
            adapter= this@HomeScreen.adapterPop
        }
        binding.cityListRv.apply {
            adapter = this@HomeScreen.adapterCityAdapter
        }
    }
    private val onClickItem : (Hotel)->Unit = {
        val bundle = bundleOf(
            "hotel" to it,
            "search" to this.search
        )
        findNavController().navigate(R.id.action_homeScreen_to_mainActivity2,bundle)
    }

    private val onClickFavorButton : (Hotel) -> Unit = {
        val authen = "Bearer ${sharePreferenceUtils.getToken(requireContext())}"
        val userId = sharePreferenceUtils.getUser(requireContext())._id
        val hotelId = it._id
        val favorBody = FavorBody(hotelId,userId)
        favorViewModel.makeFavor(userId,authen,favorBody)
    }

    private val onClickRemoveFavorButton : (Hotel) -> Unit = {
        val authen = "Bearer ${sharePreferenceUtils.getToken(requireContext())}"
        val userId = sharePreferenceUtils.getUser(requireContext())._id
        val hotelId = it._id
        val favorBody = FavorBody(hotelId,userId)
        favorViewModel.deleteFavor(userId,hotelId,authen)

    }


}