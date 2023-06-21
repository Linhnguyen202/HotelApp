package com.example.hotelapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hotelapp.R
import com.example.hotelapp.adapter.HotelFavorAdapter
import com.example.hotelapp.application.HotelApplication
import com.example.hotelapp.database.HotelDatabase
import com.example.hotelapp.databinding.FragmentSavedScreenBinding
import com.example.hotelapp.model.FavorBody
import com.example.hotelapp.model.Hotel
import com.example.hotelapp.repository.FavorRepository
import com.example.hotelapp.share.sharePreferenceUtils
import com.example.hotelapp.utils.Resource
import com.example.hotelapp.viewModel.FavorViewModel.FavorViewModel
import com.example.hotelapp.viewModel.FavorViewModel.FavorViewModelProviderFactory


class SavedScreen : Fragment() {
    lateinit var binding : FragmentSavedScreenBinding
    lateinit var adapter: HotelFavorAdapter
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
        binding = FragmentSavedScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpItem()
        addData()
        addEvent()

    }

    private fun addEvent() {
        binding.signInbutton.setOnClickListener {
            findNavController().navigate(R.id.action_savedScreen_to_mainActivity32)
        }
    }

    private fun setUpItem() {
        adapter = HotelFavorAdapter(onClickItem,onClickFavorButton,onClickRemoveFavorButton)
        binding.favorHotelRv.apply {
            adapter = this@SavedScreen.adapter
        }
    }
    private fun addData() {
        if(sharePreferenceUtils.isSharedPreferencesExist(requireContext(),"USER","TOKEN_VALUE") && sharePreferenceUtils.isSharedPreferencesExist(requireContext(),"USER","USER_VALUE")){
           binding.notSignView.visibility = View.GONE
           binding.favorHotelRv.visibility = View.VISIBLE
            val authen = "Bearer ${sharePreferenceUtils.getToken(requireContext())}"
            val userId = sharePreferenceUtils.getUser(requireContext())._id
            favorViewModel.getFavor(userId,authen)
            favorViewModel.getFavorResponse.observe(viewLifecycleOwner){
                when(it){
                    is Resource.Success -> {
                        it.data?.let { GetFavorResponse->
                            adapter.differ.submitList(GetFavorResponse.favor.toList())
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
        else{
            binding.notSignView.visibility = View.VISIBLE
            binding.favorHotelRv.visibility = View.GONE
        }

    }
    private val onClickItem : (Hotel)->Unit = {
//        val bundle = bundleOf(
//            "hotel" to it,
//            "search" to this.search
//        )
//        findNavController().navigate(R.id.action_exploreScreen_to_mainActivity2,bundle)

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
                        Toast.makeText(requireContext(),PostFavorResponse.toString(), Toast.LENGTH_LONG).show()
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
                        Toast.makeText(requireContext(),DeleteFavorResponse.toString(), Toast.LENGTH_LONG).show()
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


}