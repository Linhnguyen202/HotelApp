package com.example.hotelapp.ui.bookingPage

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hotelapp.application.HotelApplication
import com.example.hotelapp.database.HotelDatabase
import com.example.hotelapp.databinding.FragmentFeedbackScreenBinding
import com.example.hotelapp.model.FeedbackBody
import com.example.hotelapp.repository.FeedbackRepository
import com.example.hotelapp.share.sharePreferenceUtils
import com.example.hotelapp.utils.Resource
import com.example.hotelapp.viewModel.FeedbackViewModel.FeedbackViewModel
import com.example.hotelapp.viewModel.FeedbackViewModel.FeedbackViewModelProviderFactory
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class FeedbackScreen : Fragment() {

    lateinit var binding : FragmentFeedbackScreenBinding
    val args : FeedbackScreenArgs by navArgs();
    var FEEDBACK_ERROR : String? = null
    var RATING_ERROR: String? = null
    private val repository by lazy {
        FeedbackRepository(HotelDatabase(requireContext()));
    }
    private val viewModelProviderFactory by lazy {
        FeedbackViewModelProviderFactory(HotelApplication(),repository)
    }

    private val viewModel by lazy {
        ViewModelProvider(this,viewModelProviderFactory)[FeedbackViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFeedbackScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addData()
        addEvent()
        viewModel.userFeedback.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    it.data.let { FeedbackResponse ->
                        Toast.makeText(requireContext(),FeedbackResponse.toString(),Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Error ->{

                }
                is Resource.Loading -> {

                }
            }
        }
    }

    private fun addEvent() {
        binding.toolbar.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.buttonSubmit.setOnClickListener {
            if(!checkFeedback()){
                Toast.makeText(requireContext(),FEEDBACK_ERROR,Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            else if(!checkRateStar()){
                Toast.makeText(requireContext(),RATING_ERROR,Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            else{
                val auth = "Bearer "+ sharePreferenceUtils.getToken(requireContext())
                val comment = binding.editTextFeedback.text.toString()
                val rate = binding.ratingBarFeedback.rating.toDouble()
                val user = args.booking.user._id
                val hotel = args.booking.hotel._id
                val room = args.booking.room._id
                val feedbackBody = FeedbackBody(comment,hotel,rate,room,user)
                viewModel.sendFeedback(auth, user,hotel,feedbackBody)
            }
        }
        binding.sendImageSubmit.setOnClickListener {
            openStorageImage()
        }
        binding.delImageBtn.setOnClickListener{
            binding.imageFeed.setImageDrawable(null)
            binding.cardViewContainer.visibility = View.GONE
        }

    }
   private fun onSubmit(){
       val auth = "Bearer "+ sharePreferenceUtils.getToken(requireContext())
        val comment = binding.editTextFeedback.text.toString()
       val rate = binding.ratingBarFeedback.rating.toDouble()
       val user = args.booking.user._id
       val hotel = args.booking.hotel._id
       val room = args.booking.room._id
       val feedbackBody = FeedbackBody(comment,hotel,rate,room,user)
       viewModel.sendFeedback(auth, user,hotel,feedbackBody)
       
   }
    private fun openStorageImage(){
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
    }

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            val maxAllowedSize = 5 * 1024 * 1024 // 5 MB (adjust this value as needed)
            val selectedImageSize = getImageSize(uri)
            if(checkImageMimeType(requireContext(),uri) == true){
                if (selectedImageSize > maxAllowedSize) {
                    Toast.makeText(requireContext(),"Kích cỡ ảnh quá lớn",Toast.LENGTH_LONG).show()
                } else {
                    binding.imageFeed.setImageURI(uri)
                    binding.cardViewContainer.visibility = View.VISIBLE
                }
            }
            else{
                Toast.makeText(requireContext(),"Loại ảnh không hợp lệ",Toast.LENGTH_LONG).show()
            }

        } else {
            Log.d("PhotoPicker", "No media selected")
            binding.cardViewContainer.visibility = View.INVISIBLE
        }
    }
    fun getImageSize(uri: Uri): Long {
        val contentResolver = context?.contentResolver
        val inputStream = contentResolver?.openInputStream(uri)
        val fileSize = inputStream?.available() ?: 0
        inputStream?.close()
        return fileSize.toLong()
    }
    fun checkImageMimeType(context: Context, imageUri: Uri): Boolean? {
        val contentResolver: ContentResolver = context.contentResolver
        try {
            // Open an InputStream from the URI
            val inputStream: InputStream? = contentResolver.openInputStream(imageUri)

            // Check if the InputStream is not null
            if (inputStream != null) {
                // Use BitmapFactory to decode the InputStream and check if it's a JPEG image
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                BitmapFactory.decodeStream(inputStream, null, options)
                if (options.outMimeType != null && options.outMimeType == "image/jpeg") {
                    // It's a JPEG image
                    return true
                }

                // It's not a JPEG image
                inputStream.close()
            }
        } catch (e: java.lang.Exception) {
            // Handle exceptions, e.g., if the URI is invalid or the file can't be read
        }

        return false
    }

    private fun checkFeedback() : Boolean{
        if(binding.editTextFeedback.text.isNullOrEmpty()){
            FEEDBACK_ERROR = "Please write your feedback"
            return false
        }
        if(binding.editTextFeedback.length() > 100){
            FEEDBACK_ERROR = "Your feedback is under 100 characters only"
            return false
        }
        return true
    }
    private fun checkRateStar(): Boolean {
        val rate = binding.ratingBarFeedback.rating
        if(rate.equals(0)){
            RATING_ERROR = "Please rate your hotel"
           return false
        }
        else if(rate > 5){
            RATING_ERROR = "Your rating is only between 1 - 5"
        }
        return true
    }

    private fun addData() {
        val booking = args.booking
        binding.detailHotelName.text = booking.hotel.name
        binding.rateNumCardTxt.text = booking.hotel.rate.toString()
        binding.detailHotelLocation.text = booking.hotel.address
        binding.banner.setImageBitmap(getBitmapfromUrl(booking.hotel.image))
        binding.toolbar.titleToolbar.text = "Feedback "+ booking.hotel.name
    }

    fun getBitmapfromUrl(imageUrl: String?): Bitmap? {
        return try {
            val url = URL(imageUrl)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.setDoInput(true)
            connection.connect()
            val input: InputStream = connection.getInputStream()
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}