package com.example.hotelapp.ui.start

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hotelapp.R
import com.example.hotelapp.application.HotelApplication
import com.example.hotelapp.database.HotelDatabase
import com.example.hotelapp.databinding.FragmentSignUpScreenBinding
import com.example.hotelapp.model.User
import com.example.hotelapp.model.registerBody
import com.example.hotelapp.repository.AuthRepository
import com.example.hotelapp.share.sharePreferenceUtils
import com.example.hotelapp.utils.Resource
import com.example.hotelapp.viewModel.AuthViewModel.AuthViewModel
import com.example.hotelapp.viewModel.AuthViewModel.AuthViewModelProviderFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.regex.Matcher
import java.util.regex.Pattern


class SignUpScreen : Fragment(), View.OnClickListener,View.OnFocusChangeListener, View.OnKeyListener{
    lateinit var binding : FragmentSignUpScreenBinding
    lateinit var dialog: BottomSheetDialog
    lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    public val authRepository by lazy {
        AuthRepository(HotelDatabase(requireContext()));
    }
    public val authViewModelProviderFactory by lazy {
        AuthViewModelProviderFactory(HotelApplication(),authRepository)
    }
    private val authViewModel by lazy {
        ViewModelProvider(this,authViewModelProviderFactory)[AuthViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpScreenBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nameEditText.onFocusChangeListener = this
        binding.phoneEditText.onFocusChangeListener  = this
        binding.emailEditText.onFocusChangeListener = this
        binding.passEditText.onFocusChangeListener  = this
        binding.confirmPassEditText.onFocusChangeListener = this
        binding.idUserContainerEditText.onFocusChangeListener = this
        binding.signupBtn.setOnClickListener(this)
        addEvent()
    }

    private fun addEvent() {
        binding.signInTxt.setOnClickListener{
            findNavController().navigate(R.id.action_signUpScreen_to_logInScreen)
        }
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
    var NAME_ERROR : String? = null
    var PHONE_ERROR : String? = null
    var USERID_ERROR : String? = null
    var EMAIL_ERROR : String? = null
    var PASS_ERROR : String? = null
    var CONFIRMPASS_ERROR : String? = null

    private fun  validateUserId() : Boolean{
        val IdUSer = binding.idUserContainerEditText.text.toString()
        if(IdUSer.isEmpty()){
            USERID_ERROR = "CMND không được để trống"
        }
        else if(checkStringSpecialChar(IdUSer)){
            USERID_ERROR = "CMND không hợp lệ"
        }
        else {
            USERID_ERROR = null
        }
        if(USERID_ERROR != null){
            binding.idUserContainer.apply {
                isErrorEnabled = true;
                error = USERID_ERROR
            }
        }
        else {
            binding.idUserContainer.apply {
                isErrorEnabled = false;
            }
        }

        return USERID_ERROR == null
    }


    private fun  validateFullname() : Boolean{
        val fullname = binding.nameEditText.text.toString()
        val number = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]", Pattern.CASE_INSENSITIVE)
        val matcherNumber: Matcher = number.matcher(fullname)
        val containsNumber: Boolean = matcherNumber.find()
        if(fullname.isEmpty()){
            NAME_ERROR = "Họ và tên không được để trống"
        }
        else if(containsNumber) {
            NAME_ERROR = "Họ và tên không hợp lệ"
        }
        else{
            NAME_ERROR = null
        }
        if(NAME_ERROR != null){
            binding.nameContainer.apply {
                isErrorEnabled = true;
                error = NAME_ERROR
            }
        }
        else{
            binding.nameContainer.apply {
                isErrorEnabled = false;
            }
        }

        return NAME_ERROR == null
    }


    private fun  validateNumber() : Boolean{
        val number = binding.phoneEditText.text.toString()
        if(number.isEmpty()){
            PHONE_ERROR = "Số điện thoại không được để trống"
        }
        else if(number.length > 10){
            PHONE_ERROR = "Số điện thoại không được quá 10 kí tự"
        }
        else if(number.startsWith("02") || number.startsWith("05") || number.startsWith("07") || number.startsWith("08") || number.startsWith("09")){
            PHONE_ERROR = "Số điện thoại không hợp lệ"
        }
        else{
            PHONE_ERROR = null
        }
        if(PHONE_ERROR != null){
            binding.phoneContainer.apply {
                isErrorEnabled = true;
                error = PHONE_ERROR
            }
        }
        else {
            binding.phoneContainer.apply {
                isErrorEnabled = false;
            }
        }
        return PHONE_ERROR == null
    }
    private fun  validateEmail() : Boolean{
        val email = binding.emailEditText.text.toString()
        if(email.isEmpty()){
            EMAIL_ERROR = "Tài khoản email không được để trống"
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            EMAIL_ERROR = "Tài khoản email không hợp lệ"
        }
        else {
            EMAIL_ERROR = null
        }
        if(EMAIL_ERROR != null){
            binding.emailContainer.apply {
                isErrorEnabled = true;
                error = EMAIL_ERROR
            }
        }
        else {
            binding.emailContainer.apply {
                isErrorEnabled = false;
            }
        }

        return EMAIL_ERROR == null
    }
    private fun validatePassword() : Boolean{
        val password = binding.passEditText.text.toString()
        if(password.isEmpty()){
            PASS_ERROR = "Mật khẩu không được để trống"
        }
        else if(password.length <= 6){
            PASS_ERROR = "Mật khẩu ít nhất phải có 6 kí tự"
        }
        else {
            PASS_ERROR = null
        }

        if(PASS_ERROR != null){
            binding.passContainer.apply {
                isErrorEnabled = true;
                error = PASS_ERROR
            }
        }
        else {
            binding.passContainer.apply {
                isErrorEnabled = false;
            }
        }

        return PASS_ERROR == null
    }


    private fun validateConfirmPassword() : Boolean{
        val Confrimpassword = binding.confirmPassEditText.text.toString()
        val password = binding.passEditText.text.toString()
        if(Confrimpassword.isEmpty()){
            CONFIRMPASS_ERROR = "Xác nhận mật khẩu không được để trống"
        }
        else if(Confrimpassword.length <= 6){
            CONFIRMPASS_ERROR = "Xác nhận mật khẩu ít nhất phải có 6 kí tự"
        }
        else if(Confrimpassword != password) {
            CONFIRMPASS_ERROR = "Xác nhận không trùng khớp với mật khẩu"
        }
        else{
            CONFIRMPASS_ERROR = null
        }
        if(PASS_ERROR != null){
            binding.confirmPassContainer.apply {
                isErrorEnabled = true;
                error = CONFIRMPASS_ERROR
            }
        }
        else {
            binding.confirmPassContainer.apply {
                isErrorEnabled = false;
            }
        }


        return CONFIRMPASS_ERROR == null
    }

    override fun onClick(v: View?) {
        if(v != null && v.id == R.id.signupBtn){
            onSubmit()
        }
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if(view != null){
            when(view.id){
                R.id.nameEditText -> {
                    if(hasFocus){
                        if(binding.nameContainer.isErrorEnabled){
                            binding.nameContainer.isErrorEnabled = false
                        }
                    }
                    else{
                        validateFullname()
                    }
                }
                R.id.phoneEditText -> {
                    if(hasFocus){
                        if(binding.phoneContainer.isErrorEnabled){
                            binding.phoneContainer.isErrorEnabled = false
                        }
                    }
                    else{
                        validateNumber()
                    }
                }
                R.id.emailEditText -> {
                    if(hasFocus){
                        if(binding.emailContainer.isErrorEnabled){
                            binding.emailContainer.isErrorEnabled = false
                        }
                    }
                    else{
                        validateEmail()
                    }
                }
                R.id.passEditText -> {
                    if(hasFocus){
                        if(binding.passContainer.isErrorEnabled){
                            binding.passContainer.isErrorEnabled = false
                        }
                    }
                    else{
                        if(validatePassword() && binding.confirmPassEditText.text!!.isNotEmpty() && validateConfirmPassword() ){
                            if(binding.confirmPassContainer.isErrorEnabled){
                                binding.confirmPassContainer.isErrorEnabled = false
                            }
                        }
                    }
                }
                R.id.confirmPassEditText -> {
                    if(hasFocus){
                        if(binding.confirmPassContainer.isErrorEnabled){
                            binding.confirmPassContainer.isErrorEnabled = false
                        }
                    }
                    else{
                        if(validateConfirmPassword() && validatePassword()){
                            if(binding.passContainer.isErrorEnabled){
                                binding.passContainer.isErrorEnabled = false
                            }
                        }
                    }
                }
                R.id.idUserContainerEditText -> {
                    if(hasFocus){
                        if(binding.idUserContainer.isErrorEnabled){
                            binding.idUserContainer.isErrorEnabled = false
                        }
                    }
                    else{
                        validateUserId()
                    }
                }

            }
        }
    }

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if(KeyEvent.KEYCODE_ENTER == keyCode && event!!.action == KeyEvent.ACTION_UP){
            onSubmit()
        }
        return false

    }

//    private fun onSubmit() {
//        if(validateFullname() && validateUserId() && validateNumber() && validateEmail() && validatePassword() && validateConfirmPassword()){
//            val regisBody = registerBody(binding.nameEditText.text.toString(),binding.emailEditText.text.toString(),"hanoi",binding.idUserContainerEditText.text.toString(),binding.phoneEditText.text.toString(),binding.passEditText.text.toString())
//            authViewModel.makeRegister(regisBody)
//            authViewModel.user.observe(viewLifecycleOwner){
//                when(it){
//                    is Resource.Success -> {
//                            it.data.let { UserResponse->
//                                val loginUser = User(UserResponse?.user!!.__v,UserResponse?.user!!._id,UserResponse?.user!!.address,UserResponse?.user.identification,UserResponse?.user!!.email,UserResponse?.user!!.password,UserResponse?.user!!.phoneNumber,UserResponse?.user!!.username)
//                                sharePreferenceUtils.saveToken(UserResponse?.token.toString(),requireContext())
//                                sharePreferenceUtils.saveUser(loginUser,requireContext())
//                                findNavController().navigate(R.id.action_signUpScreen_to_mainActivity)
//                        }
//
//                    }
//                    is Resource.Error -> {
//                        when(it.message){
//                            "401" -> {
//                                val dialog = layoutInflater.inflate(R.layout.custom_dialog,null)
//                                val myDialog = Dialog(requireContext())
//                                myDialog.setContentView(dialog)
//                                myDialog.setCancelable(true)
//                                myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//                                myDialog.show()
//                                val subBtn = dialog.findViewById<Button>(R.id.submitButton)
//                                subBtn.setOnClickListener {
//                                    myDialog.dismiss()
//                                }
//                                Toast.makeText(requireContext(),"Mật khẩu không đúng",Toast.LENGTH_LONG).show()
//                            }
//                            "402" -> {
//                                Toast.makeText(requireContext(),"Tài khoản email đã tồn tại",Toast.LENGTH_LONG).show()
//                            }
//
//                        }
//                    }
//                    is Resource.Loading -> {
//                        Toast.makeText(requireContext(),"Loading",Toast.LENGTH_LONG).show()
//                    }
//
//                }
//            }
//        }
//    }

    private fun validate() : Boolean{
        var isValid = true;
        if(!validateEmail()) isValid = false
        if(!validatePassword()) isValid = false
        if(!validateNumber()) isValid= false
        if(!validateUserId()) isValid = false
        if(!validateConfirmPassword()) isValid = false
        return isValid;
    }
    private fun onSubmit() {
        if(validateFullname() && validateUserId() && validateNumber() && validateEmail() && validatePassword() && validateConfirmPassword()){
            val regisBody = registerBody(binding.nameEditText.text.toString(),binding.emailEditText.text.toString(),"hanoi",binding.idUserContainerEditText.text.toString(),binding.phoneEditText.text.toString(),binding.passEditText.text.toString())
            authViewModel.makeRegister(regisBody)
            authViewModel.user.observe(viewLifecycleOwner){
                when(it){
                    is Resource.Success -> {
                            it.data.let { UserResponse->
                                val loginUser = User(UserResponse?.user!!.__v,UserResponse?.user!!._id,UserResponse?.user!!.address,UserResponse?.user.identification,UserResponse?.user!!.email,UserResponse?.user!!.password,UserResponse?.user!!.phoneNumber,UserResponse?.user!!.username)
                                sharePreferenceUtils.saveToken(UserResponse?.token.toString(),requireContext())
                                sharePreferenceUtils.saveUser(loginUser,requireContext())
                                findNavController().navigate(R.id.action_signUpScreen_to_mainActivity)
                        }

                    }
                    is Resource.Error -> {
                       if(it.message == "404") {
                           Toast.makeText(requireContext(),"Không thể đăng ký tài khoản",Toast.LENGTH_LONG)
                       }
                        else {
                           Toast.makeText(requireContext(),"Tải khoản đã tồn tại",Toast.LENGTH_LONG)
                       }

                    }
                    is Resource.Loading -> {
                        Toast.makeText(requireContext(),"Loading",Toast.LENGTH_LONG).show()
                    }

                }
            }
        }
    }


}