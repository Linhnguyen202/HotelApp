package com.example.hotelapp.ui

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.util.Pair
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
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class SearchForm : BottomSheetDialogFragment(), TextWatcher, View.OnClickListener,View.OnFocusChangeListener, View.OnKeyListener{
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
    private fun  validateCity(value: String) : String{
        var nameError: String? = ""
        val number = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]", Pattern.CASE_INSENSITIVE)
        val matcherNumber: Matcher = number.matcher(value)
        val containsNumber: Boolean = matcherNumber.find()
        if(value.isEmpty()){
            nameError = "Thành phố không được để trống"
        }
        else if(containsNumber) {
            nameError = "Thành phố không hợp lệ"
        }

        return nameError.toString()
    }
    private fun  validateRoom(value: String) : String{
        var nameError: String? = ""
        val room = binding.roomNumberTxt.text.toString()
        if(room.isEmpty()){
            nameError = "Số phòng không được để trống"
        }
        else if(checkStringSpecialChar(room)) {
            nameError = "Số phòng không hợp lệ"
        }
        else if(room.toInt() > 100 || room.toInt() <= 0) {
            nameError = "Số phòng giới hạn trong khoảng từ giới hạn 1 đến 100 phòng"
        }
        return nameError.toString()
    }

    private fun  validateGuest(value: String) : String{
        var nameError: String? = ""
        val guest = binding.adultsNumberTxt.text.toString()
        if(guest.isEmpty() || guest.toInt() == 0){
            nameError = "Số người không được để trống"
            return nameError
        }
        else if(checkStringSpecialChar(guest)) {
            nameError = "Số người không hợp lệ"
            return nameError
        }
        else if(guest.toInt() > 10 || guest.toInt() < 0) {
            nameError = "Số người giới hạn trong khoảng từ giới hạn 1 đến 10 người"
            return nameError
        }
        return nameError.toString()
    }
    private fun validateDate(value: String) : String {
        var nameError: String? = ""
        val time = binding.timePick.text
        val words = binding.timePick.text.split("-")
        if(time.isEmpty()){
            nameError = "Thời gian không được để trống"
        }
        else if(words.size != 2 || !isDateValid(words[0].toString()) || !isDateValid(words[1].toString())){
            nameError = "Thời gian không hợp lệ"
        }
        return nameError.toString()
    }

    fun isDateValid(dateStr: String): Boolean {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        sdf.isLenient = false // This makes the date validation strict.
        val date = sdf.parse(dateStr)
        return date != null
    }
    private fun validate() : Boolean {
        var isValid = true;
        var errorMessage : String? = null
        val city = binding.searchInput.text.toString()
        val room = binding.roomNumberTxt.text.toString()
        val guest = binding.adultsNumberTxt.text.toString()
        val time = binding.timePick.text.toString()
        if(validateCity(city).isNotEmpty()) {
            Toast.makeText(requireContext(),validateCity(city),Toast.LENGTH_LONG).show()
            return false
        }
        if(validateDate(time).isNotEmpty()){
            Toast.makeText(requireContext(),validateDate(time),Toast.LENGTH_LONG).show()
            return false
        }
        if(validateRoom(room).isNotEmpty()) {
            Toast.makeText(requireContext(),validateRoom(room),Toast.LENGTH_LONG).show()
            return false
        }
        if(validateGuest(guest).isNotEmpty()){
            Toast.makeText(requireContext(),validateGuest(guest),Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun addEvents() {
        // get time
        addTimePicker()
        binding.toolbar.backButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.searchBtn.setOnClickListener {
             if(validate()){
                var placeSearch = binding.searchInput.text.toString().trim();
                var rooms = binding.roomNumberTxt.text.toString()
                var adult = binding.adultsNumberTxt.text.toString()
                var timePick = binding.timePick.text.toString()
                var searchData = Search(placeSearch,timePick,rooms,adult,"")
                hotelViewModel.setDataSearch(searchData)
                binding.searchInput.text.clear()
                dismiss()
             }
            else{

             }
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

        // click to show date picker
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
//            binding.searchBtn.isEnabled = false
//            binding.searchBtn.setBackgroundColor(android.graphics.Color.parseColor("#66c50a27"))
        }
        else{
//            binding.searchBtn.isEnabled = true
//            binding.searchBtn.setBackgroundColor(android.graphics.Color.parseColor("#c50a27"))

        }
    }

    override fun afterTextChanged(s: Editable?) {

    }

    override fun onStop() {
        super.onStop()
        binding.searchInput.setText("")

    }

    override fun onClick(v: View?) {
        if(v != null && v.id == R.id.signupBtn){
//            onSubmit()
        }
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if(view != null){
            when(view.id){
                R.id.searchInput -> {
                    if(hasFocus){
                        binding.searchError.visibility = View.GONE
                        binding.searchError.text = ""
                    }
                    else{

                    }
                }
                R.id.roomNumberTxt -> {

                }
                R.id.adultsNumberTxt -> {

                }

            }
        }
    }

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if(KeyEvent.KEYCODE_ENTER == keyCode && event!!.action == KeyEvent.ACTION_UP){
//            onSubmit()
        }
        return false
    }


}