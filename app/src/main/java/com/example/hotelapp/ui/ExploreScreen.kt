package com.example.hotelapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.hotelapp.R
import com.example.hotelapp.adapter.HotelMainAdapter
import com.example.hotelapp.application.HotelApplication
import com.example.hotelapp.database.HotelDatabase
import com.example.hotelapp.databinding.FragmentExploreScreenBinding
import com.example.hotelapp.model.FavorBody
import com.example.hotelapp.model.Hotel
import com.example.hotelapp.model.Search
import com.example.hotelapp.repository.FavorRepository
import com.example.hotelapp.repository.HotelRepository
import com.example.hotelapp.share.sharePreferenceUtils
import com.example.hotelapp.utils.Resource
import com.example.hotelapp.utils.loadingDialog
import com.example.hotelapp.viewModel.FavorViewModel.FavorViewModel
import com.example.hotelapp.viewModel.FavorViewModel.FavorViewModelProviderFactory
import com.example.hotelapp.viewModel.HotelViewModel
import com.example.hotelapp.viewModel.HotelViewModelProviderFactory
import kotlinx.coroutines.flow.collect


class ExploreScreen : Fragment() {
    lateinit var binding : FragmentExploreScreenBinding
    lateinit var adapter: HotelMainAdapter
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
    private val favorRepository by lazy {
        FavorRepository(HotelDatabase(requireContext()));
    }
    private val favorViewModelProviderFactory by lazy {
        FavorViewModelProviderFactory(HotelApplication(),favorRepository)
    }
    private val favorViewModel by lazy {
        ViewModelProvider(this,favorViewModelProviderFactory)[FavorViewModel::class.java]
    }
    private val parentHotelViewModel by lazy {
        ViewModelProvider(requireActivity(),viewModelProviderFactory)[HotelViewModel::class.java]
    }
    private var dataSearch : Search? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
       binding = FragmentExploreScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpItem()
        addData()
        addEvent()


    }

    private fun addEvent() {
        val searchForm = SearchForm()
        binding.hotelList.apply {
            adapter = this@ExploreScreen.adapter
        }
        binding.searchBox.setOnClickListener {
            searchForm.show(requireFragmentManager(),searchForm.tag)
        }
        binding.searchTxt.setOnClickListener{
            searchForm.show(requireFragmentManager(),searchForm.tag)
        }

    }
    private fun addData() {
        hotelViewModel.setDataType("ALL");
        lifecycleScope.launchWhenCreated {
            hotelViewModel.hotelListPage.collect{
                adapter.submitData(it)
            }
        }
        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collect{
                val state = it.refresh
                binding.progessBar.isVisible = state is LoadState.Loading
            }
        }
        parentHotelViewModel.searchData.observe(viewLifecycleOwner){
//            handleSearchingData(it)
        }

    }
//    private fun handleSearchingData(search: Search){
//        this.search = search
//        val cityQuery: String = search.place.toString()
//        val guestNumber: Int = search.adults?.toInt() ?: 0
//        if(cityQuery != ""){
//            hotelViewModel.getSearching(cityQuery,guestNumber)
//            hotelViewModel.hotelListSearching.observe(viewLifecycleOwner){
//                when(it){
//                    is Resource.Success -> {
//                        it.data?.let { HotelResponse->
//                            adapter.differ.submitList(HotelResponse.result.toList())
//                        }
//                    }
//                    is Resource.Error -> {
//
//                    }
//                    is Resource.Loading -> {
//                        Toast.makeText(requireContext(),"Loading data",Toast.LENGTH_LONG).show()
//                    }
//                }
//            }
//        }
//        else{
//            hotelViewModel.hotelList.observe(viewLifecycleOwner){
//                when(it){
//                    is Resource.Success -> {
//                        it.data?.let { HotelResponse->
//                            adapter.differ.submitList(HotelResponse.result.toList())
//                        }
//                    }
//                    is Resource.Error -> {
//
//                    }
//                    is Resource.Loading -> {
//                        Toast.makeText(requireContext(),"Loading data",Toast.LENGTH_LONG).show()
//                    }
//                }
//            }
//        }
//
//    }
    fun setData(search: Search) {
       Toast.makeText(requireContext(),search.toString(),Toast.LENGTH_LONG).show()
    }
    private fun setUpItem() {
        adapter = HotelMainAdapter(onClickItem,onClickFavorButton,onClickRemoveFavorButton,checkFavorHotel)
    }

    private val onClickItem : (Hotel)->Unit = {
        val bundle = bundleOf(
            "hotel" to it,
            "search" to this.search
        )
        findNavController().navigate(R.id.action_exploreScreen_to_mainActivity2,bundle)

    }
    private val onClickFavorButton : (Hotel) -> Unit = {
        val authen = "Bearer ${sharePreferenceUtils.getToken(requireContext())}"
        val userId = sharePreferenceUtils.getUser(requireContext())._id
        val hotelId = it._id
        val favorBody = FavorBody(hotelId,userId)
        favorViewModel.makeFavor(userId,authen,favorBody)
        favorViewModel.postFavorResponse.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    it.data?.let { PostFavorResponse->
                      Toast.makeText(requireContext(),PostFavorResponse.toString(),Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {
                    Toast.makeText(requireContext(),"Loading data",Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private val onClickRemoveFavorButton : (Hotel) -> Unit = {
        val authen = "Bearer ${sharePreferenceUtils.getToken(requireContext())}"
        val userId = sharePreferenceUtils.getUser(requireContext())._id
        val hotelId = it._id
        val favorBody = FavorBody(hotelId,userId)
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
    }

    private val checkFavorHotel : (Hotel) -> Boolean = {
        var isExist = 0
//        val authen = "Bearer ${sharePreferenceUtils.getToken(requireContext())}"
//        val userId = sharePreferenceUtils.getUser(requireContext())._id
//        val _id = it._id
//        favorViewModel.checkFavor(userId, _id, authen)
//        favorViewModel.checkFavorResponse.observe(viewLifecycleOwner){
//            when(it){
//                is Resource.Success -> {
//                    it.data?.let { PostFavorResponse->
//                           isExist = PostFavorResponse.message.toInt()
//                    }
//                }
//                is Resource.Error -> {
//                    isExist = 0
//                }
//                is Resource.Loading -> {
//                    Toast.makeText(requireContext(),"Loading data",Toast.LENGTH_LONG).show()
//                }
//            }
//        }
//        Toast.makeText(requireContext(),isExist.toString(),Toast.LENGTH_SHORT).show()
        isExist == 1
    }

}