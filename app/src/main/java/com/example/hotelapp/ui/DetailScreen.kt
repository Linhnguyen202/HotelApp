package com.example.hotelapp.ui


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log

import android.view.*
import android.widget.*

import androidx.appcompat.app.AppCompatActivity

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.hotelapp.MainActivity2
import com.example.hotelapp.R
import com.example.hotelapp.adapter.HotelOfferAdapter
import com.example.hotelapp.adapter.RoomTypeAdapter
import com.example.hotelapp.databinding.FragmentDetailScreenBinding
import com.example.hotelapp.model.Hotel
import com.example.hotelapp.model.HotelOffer
import com.example.hotelapp.model.RoomType
import com.example.hotelapp.model.Search
import com.example.hotelapp.share.sharePreferenceUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class DetailScreen : Fragment(){
    lateinit var binding : FragmentDetailScreenBinding
    lateinit var adapterRoomType: RoomTypeAdapter
    private var DATA : Hotel? = null
    private val adapterHotelOffer : HotelOfferAdapter by lazy {
        HotelOfferAdapter()
    }
    private val roomTypeList : MutableList<RoomType> = mutableListOf()
    private val hotelOfferList : MutableList<HotelOffer> = mutableListOf()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
       binding = FragmentDetailScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity?)!!.getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.back)
        if( (activity as AppCompatActivity?)!!.supportActionBar != null){
            (activity as AppCompatActivity?)!!.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            (activity as AppCompatActivity?)!!.supportActionBar?.title = null
        }
        binding.toolbar.setNavigationOnClickListener {
            (activity as MainActivity2?)!!.finish()
        }
        addData()
        addEvents()
    }

    private fun addEvents() {
        val bundle = bundleOf(
            "id" to DATA?._id,
            "name" to DATA?.name
        )
        binding.checkRoomBtn.setOnClickListener {
            // check user login
            if(sharePreferenceUtils.isSharedPreferencesExist(requireContext(),"USER","TOKEN_VALUE") && sharePreferenceUtils.isSharedPreferencesExist(requireContext(),"USER","USER_VALUE")){
                val search = (activity as MainActivity2).args.search
                if(search.place.isNullOrEmpty() || search.room.isNullOrEmpty()|| search.adults.isNullOrEmpty()){
                    handelBottomSheetDialog(bundle)
                }
                else{
                    findNavController().navigate(R.id.action_detailScreen_to_roomListScreen,bundle)
                }

            }
            else{
                findNavController().navigate(R.id.action_detailScreen_to_mainActivity3)
            }

        }

        binding.chatBtn.setOnClickListener {
            if(sharePreferenceUtils.isSharedPreferencesExist(requireContext(),"USER","TOKEN_VALUE") && sharePreferenceUtils.isSharedPreferencesExist(requireContext(),"USER","USER_VALUE")){
                val hotelId = DATA?._id
                val user = sharePreferenceUtils.getUser(requireContext())
                val bundle = bundleOf(
                    "hotel" to hotelId,
                    "user" to user._id,
                )
                findNavController().navigate(R.id.action_detailScreen_to_chatScreen,bundle)
            }
            else{
                findNavController().navigate(R.id.action_detailScreen_to_mainActivity3)
            }
        }
    }
    private fun handelBottomSheetDialog(bundle: Bundle){
        val viewDialg : View = layoutInflater.inflate(R.layout.layout_bottom_sheet,null)
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(viewDialg)
        bottomSheetDialog.show()


        val datePickerBox = viewDialg.findViewById<ConstraintLayout>(R.id.datePickBoxBottom)
        val timePick = viewDialg.findViewById<EditText>(R.id.timePick)
        val submitBtn = viewDialg.findViewById<Button>(R.id.submitButton)
        val cancelBtn = viewDialg.findViewById<Button>(R.id.cancelButton)
        // room
        val plusRoom = viewDialg.findViewById<ImageView>(R.id.plusRoomIcon)
        val misnusRoom = viewDialg.findViewById<ImageView>(R.id.minusRoomIcon)
        val roomNumber = viewDialg.findViewById<EditText>(R.id.roomNumberTxt)

        addMoreData(plusRoom,roomNumber)
        removeData(misnusRoom,roomNumber)

        // guest
        val plusGuest = viewDialg.findViewById<ImageView>(R.id.plusAdultsIcon)
        val misnusGuest = viewDialg.findViewById<ImageView>(R.id.minusAdultsIcon)
        val guestNumber = viewDialg.findViewById<EditText>(R.id.adultsNumberTxt)
        addMoreData(plusGuest,guestNumber)
        removeData(misnusGuest,guestNumber)



        // Set the date range
        val calendar = Calendar.getInstance()
        val calendarStart = Calendar.getInstance()
        calendarStart.set(2023, calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE))
        val calendarEnd = Calendar.getInstance()
        calendarEnd.set(2023, calendar.get(Calendar.MONTH)+2,1)

        // Set the date picker constraints
        val constraintsBuilder = CalendarConstraints.Builder()
        val dateValidatorMin = DateValidatorPointForward.from(calendarStart.timeInMillis)
        val dateValidatorMax = DateValidatorPointBackward.before(calendarEnd.timeInMillis)
        constraintsBuilder.setValidator(CompositeDateValidator.allOf(listOf( dateValidatorMin, dateValidatorMax)))
        val constraints = constraintsBuilder.build()
        // open date picker


        val datePicker = MaterialDatePicker.Builder
            .dateRangePicker()
            .setCalendarConstraints(constraints)
            .setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(),
                MaterialDatePicker.todayInUtcMilliseconds()))
            .build()
        datePickerBox.setOnClickListener {
            datePicker.show(parentFragmentManager,"Matarial Manager")
            datePicker.addOnPositiveButtonClickListener {
                val simpleDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                timePick.setText("${simpleDate.format(it.first)}-${simpleDate.format(it.second)}")
            }
        }
//        val textWatcher = object : TextWatcher{
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//               if(timePick.text.isEmpty() ||roomNumber.text.toString().toInt() == 0 || adultNumber.text.toString().toInt() == 0){
//                   submitBtn.isEnabled = false
//                   submitBtn.setBackgroundColor(android.graphics.Color.parseColor("#66c50a27"))
//               }
//                else{
//                    submitBtn.isEnabled = true
//                   submitBtn.setBackgroundColor(android.graphics.Color.parseColor("#c50a27"))
//               }
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//
//            }
//
//        }
//        timePick.addTextChangedListener(textWatcher)
//        roomNumber.addTextChangedListener(textWatcher)
//        adultNumber.addTextChangedListener(textWatcher)

        submitBtn.setOnClickListener {
            val timeValue = timePick.text.toString()
            val roomValue = roomNumber.text.toString()
            val guestValue = guestNumber.text.toString()



            if(validateDate(viewDialg) && validateRoom(viewDialg) && validateGuest(viewDialg)){
                (activity as MainActivity2).args.search.place = DATA!!.address
                (activity as MainActivity2).args.search.time = timeValue
                (activity as MainActivity2).args.search.room = roomValue
                (activity as MainActivity2).args.search.adults = guestValue
                findNavController().navigate(R.id.action_detailScreen_to_roomListScreen,bundle)
                bottomSheetDialog.dismiss()
            }
        }
        cancelBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
        }


    }

    private fun validate(timeEdit : EditText, roomEdit : EditText,guestEdit : EditText) : Boolean{
        val timeValue = timeEdit.text.toString()
        val roomValue = roomEdit.text.toString()
        val guestValue = guestEdit.text.toString()

//        if(validateDate(timeValue).isNotEmpty()){
//            Toast.makeText(requireContext(),validateDate(timeValue), Toast.LENGTH_LONG).show()
//            return false
//        }
//        if(validateRoom(roomValue).isNotEmpty()) {
//            Toast.makeText(requireContext(),validateRoom(roomValue), Toast.LENGTH_LONG).show()
//            return false
//        }
//        if(validateGuest(guestValue).isNotEmpty()){
//            Toast.makeText(requireContext(),validateGuest(guestValue), Toast.LENGTH_LONG).show()
//            return false
//        }
        return true
    }

    private fun validateDate(view: View) : Boolean {
        val timeValue = view.findViewById<TextView>(R.id.timePick)
        val timError =view.findViewById<TextView>(R.id.txtTimeError)
        var dateError: String? = ""
        val words = timeValue.text.split("-")
        if(timeValue.text.isEmpty()){
            dateError = "Thời gian không được để trống"
        }
        else if(words.size != 2 || !isDateValid(words[0].toString()) || !isDateValid(words[1].toString())){
            dateError = "Thời gian không hợp lệ"
        }
        else {
            dateError = null
        }
        if(dateError != null){
            timError.visibility = View.VISIBLE
            timError.text = dateError
        }
        else {
            timError.visibility = View.GONE
        }
        return dateError == null
    }

    private fun checkStringSpecialChar(value : String) : Boolean{
        val special = Pattern.compile("[a-zA-z]", Pattern.CASE_INSENSITIVE)
        val number = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]", Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = special.matcher(value)
        val matcherNumber: Matcher = number.matcher(value)
        val constainsSymbols: Boolean = matcher.find()
        val containsNumber: Boolean = matcherNumber.find()
        if(constainsSymbols || containsNumber){
            return true
        }
        return false
    }
    private fun  validateRoom(view: View) : Boolean{
        val roomValue = view.findViewById<TextView>(R.id.roomNumberTxt)
        val roomError =view.findViewById<TextView>(R.id.txtRoomError)
        var nameError: String? = ""
        if(roomValue.text.isEmpty() || roomValue.text.toString().toInt() == 0){
            nameError = "Số phòng không được để trống"
        }
        else if(checkStringSpecialChar(roomValue.text.toString())) {
            nameError = "Số phòng không hợp lệ"
        }
        else if(roomValue.text.toString().toInt() > 100 || roomValue.text.toString().toInt() < 0) {
            nameError = "Số phòng giới hạn trong khoảng từ giới hạn 1 đến 100 phòng"
        }
        else {
            nameError = null
        }
        if(nameError != null){
            roomError.visibility = View.VISIBLE
            roomError.text = nameError
        }
        else {
            roomError.visibility = View.GONE
        }
        return nameError == null
    }

    private fun  validateGuest(view : View) : Boolean{
        val guestValue = view.findViewById<TextView>(R.id.adultsNumberTxt)
        val guestError =view.findViewById<TextView>(R.id.txtGuestError)
        var valueError: String? = ""
        if(guestValue.text.toString().isEmpty() || guestValue.text.toString().toInt() == 0){
            valueError = "Số người không được để trống"
        }
        else if(checkStringSpecialChar(guestValue.text.toString())) {
            valueError = "Số người không hợp lệ"
        }
        else if(guestValue.text.toString().toInt() > 10 || guestValue.text.toString().toInt() < 0) {
            valueError = "Số người giới hạn trong khoảng từ giới hạn 1 đến 10 người"
        }
        else{
            valueError = null
        }
        if(valueError != null){
            guestError.visibility = View.VISIBLE
            guestError.text = valueError
        }
        else {
            guestError.visibility = View.GONE
        }
        return valueError == null
    }


    fun isDateValid(dateStr: String): Boolean {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        sdf.isLenient = false // This makes the date validation strict.
        val date = sdf.parse(dateStr)
        return date != null
    }





    private fun removeData(button : ImageView, textView: TextView){
        button.setOnClickListener{
            var number = textView.text.toString().toInt()
            if(number > 0){
                number--
                textView.text = number.toString()
            }
        }
    }
    private fun addMoreData(button : ImageView, textView: TextView){
        button.setOnClickListener {
            val number = textView.text.toString().toInt() + 1
            textView.text =number.toString()
        }

    }



    private fun addData() {
        DATA =  (activity as? MainActivity2)?.args?.hotel
        binding.detailHotelName.text = DATA?.name
        binding.detailHotelLocation.text = DATA?.address + "," + DATA?.city
        binding.rateNumCardTxt.text = DATA?.rate.toString()
        binding.overviewTxt.text = DATA?.description
        Glide.with(this).load(DATA!!.image).into(binding.banner)
        adapterRoomType = RoomTypeAdapter()
        roomTypeList.add(RoomType("1","bedroom 1","1 king bed"))
        roomTypeList.add(RoomType("2","bedroom 2","1 queen bed"))
        roomTypeList.add(RoomType("3","bedroom 3","1 sofa bed"))
        roomTypeList.add(RoomType("4","bedroom 4","1 king bed"))
        roomTypeList.add(RoomType("5","bedroom 5","1 king bed"))
        adapterRoomType.differ.submitList(roomTypeList)
        binding.roomTypeRv.apply {
            adapter = this@DetailScreen.adapterRoomType
        }

        // hotel offer
        hotelOfferList.add(HotelOffer("1","Beach View"))
        hotelOfferList.add(HotelOffer("2","Courtyard view"))
        hotelOfferList.add(HotelOffer("3","Ocean view"))
        hotelOfferList.add(HotelOffer("4","Free parking on premises"))
        hotelOfferList.add(HotelOffer("5","Private hot tub"))
        hotelOfferList.add(HotelOffer("6","45\" HDTV with premium cable, standard cable"))
        adapterHotelOffer.differ.submitList(hotelOfferList)
        binding.offerTypeRv.apply {
            adapter = this@DetailScreen.adapterHotelOffer
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater!!.inflate(R.menu.toolbar_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

}