package com.example.hotelapp.ui

import android.app.Dialog
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.res.Resources
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.util.Pair
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import com.example.hotelapp.R
import com.example.hotelapp.application.HotelApplication
import com.example.hotelapp.database.HotelDatabase
import com.example.hotelapp.databinding.FragmentSearchFormBinding
import com.example.hotelapp.model.Search
import com.example.hotelapp.repository.HotelRepository
import com.example.hotelapp.viewModel.HotelViewModel
import com.example.hotelapp.viewModel.HotelViewModelProviderFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.*
import java.text.SimpleDateFormat
import java.util.*


class SearchForm : BottomSheetDialogFragment(), TextWatcher{
    lateinit var binding : FragmentSearchFormBinding
    lateinit var dialog: BottomSheetDialog
    lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private val repository by lazy {
        HotelRepository(HotelDatabase(requireContext()));
    }
    private val viewModelProviderFactory by lazy {
        HotelViewModelProviderFactory(HotelApplication(),repository)
    }
    private val hotelViewModel by lazy {
        ViewModelProvider(requireActivity(),viewModelProviderFactory)[HotelViewModel::class.java]
    }
    var ROOMS : Int = 0
    var ADULTS : Int = 0
    var CHILDREN : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchFormBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        val layout : CoordinatorLayout? = dialog.findViewById(R.id.searchContainer)
        if (layout != null) {
            layout.minimumHeight = Resources.getSystem().displayMetrics.heightPixels
        }

        addEvents()

    }
    private fun checkValueSearch(){

    }
    private fun addEvents() {
        // get time
        addTimePicker()
        binding.toolbar.backButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.searchBtn.setOnClickListener {
            var placeSearch = binding.searchInput.text.toString().trim();
            var rooms = binding.roomNumberTxt.text.toString()
            var adult = binding.adultsNumberTxt.text.toString()
            var timePick = binding.timePick.text.toString()
            var searchData = Search(placeSearch,timePick,rooms,adult,"")
            hotelViewModel.setDataSearch(searchData)
            binding.searchInput.text.clear()
            dismiss()
        }

        addMoreData(binding.plusRoomIcon, binding.roomNumberTxt)
        removeData(binding.minusRoomIcon, binding.roomNumberTxt)

        addMoreData(binding.plusAdultsIcon, binding.adultsNumberTxt)
        removeData(binding.minusAdultsIcon, binding.adultsNumberTxt)



        // handle enable button
        binding.searchInput.addTextChangedListener(this)
        binding.timePick.addTextChangedListener(this)
        binding.roomNumberTxt.addTextChangedListener(this)
        binding.adultsNumberTxt.addTextChangedListener(this)

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


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        return dialog
    }
    private fun addTimePicker() {
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
        val datePicker = MaterialDatePicker.Builder
            .dateRangePicker()
            .setCalendarConstraints(constraints)
            .setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(),
                MaterialDatePicker.todayInUtcMilliseconds()))
            .build()

        binding.datePickerBox.setOnClickListener {
            datePicker.show(parentFragmentManager,"Matarial Manager")
            datePicker.addOnPositiveButtonClickListener {
                val simpleDate = SimpleDateFormat("dd/MM/yyyy",Locale.getDefault())
                val newDate : String = "${simpleDate.format(it.first)}-${simpleDate.format(it.second)}"
                binding.timePick.setText(newDate)
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val enteredText = s.toString().trim()
        if(enteredText.isNullOrEmpty() || enteredText.isBlank() || enteredText.equals("0")){
            binding.searchBtn.isEnabled = false
            binding.searchBtn.setBackgroundColor(android.graphics.Color.parseColor("#66c50a27"))
        }
        else{
            binding.searchBtn.isEnabled = true
            binding.searchBtn.setBackgroundColor(android.graphics.Color.parseColor("#c50a27"))

        }
    }

    override fun afterTextChanged(s: Editable?) {

    }

    override fun onStop() {
        super.onStop()
        binding.searchInput.setText("")

    }


}