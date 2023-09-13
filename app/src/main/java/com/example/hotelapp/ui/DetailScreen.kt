package com.example.hotelapp.ui


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher

import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

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
import com.example.hotelapp.share.sharePreferenceUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.*
import java.text.SimpleDateFormat
import java.util.*


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
    }
    private fun handelBottomSheetDialog(bundle: Bundle){
        val viewDialg : View = layoutInflater.inflate(R.layout.layout_bottom_sheet,null)
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(viewDialg)
        bottomSheetDialog.show()
        val datePickerBox = viewDialg.findViewById<ConstraintLayout>(R.id.datePickBoxBottom)
        val timePick = viewDialg.findViewById<TextView>(R.id.timePick)
        val submitBtn = viewDialg.findViewById<Button>(R.id.submitButton)
        val cancelBtn = viewDialg.findViewById<Button>(R.id.cancelButton)
        // room
        val plusRoom = viewDialg.findViewById<ImageView>(R.id.plusRoomIcon)
        val misnusRoom = viewDialg.findViewById<ImageView>(R.id.minusRoomIcon)
        val roomNumber = viewDialg.findViewById<TextView>(R.id.roomNumberTxt)

        addMoreData(plusRoom,roomNumber)
        removeData(misnusRoom,roomNumber)

        // adults
        val plusAdult = viewDialg.findViewById<ImageView>(R.id.plusAdultsIcon)
        val misnusAdult = viewDialg.findViewById<ImageView>(R.id.minusAdultsIcon)
        val adultNumber = viewDialg.findViewById<TextView>(R.id.adultsNumberTxt)
        addMoreData(plusAdult,adultNumber)
        removeData(misnusAdult,adultNumber)

        // children
        val plusChild = viewDialg.findViewById<ImageView>(R.id.plusChildrenIcon)
        val misnusChild = viewDialg.findViewById<ImageView>(R.id.minusChildrenIcon)
        val childNumber = viewDialg.findViewById<TextView>(R.id.childrenNumberTxt)

        addMoreData(plusChild,childNumber)
        removeData(misnusChild,childNumber)


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
                timePick.text = "${simpleDate.format(it.first)}-${simpleDate.format(it.second)}"
            }
        }
        val textWatcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               if(timePick.text.isEmpty() ||roomNumber.text.toString().toInt() == 0 || adultNumber.text.toString().toInt() == 0 || childNumber.text.toString().toInt() == 0){
                   submitBtn.isEnabled = false
                   submitBtn.setBackgroundColor(android.graphics.Color.parseColor("#66c50a27"))
               }
                else{
                    submitBtn.isEnabled = true
                   submitBtn.setBackgroundColor(android.graphics.Color.parseColor("#c50a27"))
               }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        }
        timePick.addTextChangedListener(textWatcher)
        roomNumber.addTextChangedListener(textWatcher)
        adultNumber.addTextChangedListener(textWatcher)
        childNumber.addTextChangedListener(textWatcher)
        submitBtn.setOnClickListener {
            (activity as MainActivity2).args.search.time = timePick.text.toString()
            (activity as MainActivity2).args.search.room = roomNumber.text.toString()
            (activity as MainActivity2).args.search.adults = adultNumber.text.toString()
            (activity as MainActivity2).args.search.children = childNumber.text.toString()
            findNavController().navigate(R.id.action_detailScreen_to_roomListScreen,bundle)
            bottomSheetDialog.dismiss()
        }
        cancelBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
        }


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