package com.example.hotelapp.ui.start

import android.app.Dialog
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hotelapp.MainActivity
import com.example.hotelapp.R
import com.example.hotelapp.application.HotelApplication
import com.example.hotelapp.database.HotelDatabase
import com.example.hotelapp.databinding.FragmentLogInScreenBinding
import com.example.hotelapp.databinding.FragmentSignUpScreenBinding
import com.example.hotelapp.model.SignInBody
import com.example.hotelapp.model.User
import com.example.hotelapp.model.registerBody
import com.example.hotelapp.repository.AuthRepository
import com.example.hotelapp.share.sharePreferenceUtils
import com.example.hotelapp.utils.Resource
import com.example.hotelapp.viewModel.AuthViewModel.AuthViewModel
import com.example.hotelapp.viewModel.AuthViewModel.AuthViewModelProviderFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.w3c.dom.Text


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
        binding.signupBtn.setOnClickListener(this)
        addEvent()
    }

    private fun addEvent() {
        binding.signInTxt.setOnClickListener{
            findNavController().navigate(R.id.action_signUpScreen_to_logInScreen)
        }
    }


    private fun  validateFullname() : Boolean{
        var nameError: String? = null
        val fullname = binding.nameEditText.text.toString()
        if(fullname.isEmpty()){
            nameError = "fullname is required"
        }
        if(nameError != null){
            binding.nameContainer.apply {
                isErrorEnabled = true;
                error = nameError
            }
        }

        return nameError == null
    }
    private fun  validateNumber() : Boolean{
        var phoneError: String? = null
        val number = binding.phoneEditText.text.toString()
        if(number.isEmpty()){
            phoneError = "Phone number is required"
        }
        if(phoneError != null){
            binding.phoneContainer.apply {
                isErrorEnabled = true;
                error = phoneError
            }
        }

        return phoneError == null
    }
    private fun  validateEmail(shouldUpdateView: Boolean = true) : Boolean{
        var emailError: String? = null
        val email = binding.emailEditText.text.toString()
        if(email.isEmpty()){
            emailError = "Email is required"
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailError = "Email address is invalid"
        }
        if(emailError != null && shouldUpdateView){
            Toast.makeText(requireContext(),emailError, Toast.LENGTH_SHORT).show()
            binding.emailContainer.apply {
                isErrorEnabled = true;
                error = emailError
            }
        }

        return emailError == null
    }
    private fun validatePassword(shouldUpdateView: Boolean = true) : Boolean{
        var passError: String? = null
        val password = binding.passEditText.text.toString()
        if(password.isEmpty()){
            passError = "Email is required"
        }
        else if(password.length <= 6){
            passError = "Password must be 6 characters long"
        }

        if(passError != null){
            binding.passContainer.apply {
                isErrorEnabled = true;
                error = passError
            }
        }

        return passError == null
    }

    private fun validateConfirmPassword(shouldUpdateView: Boolean = true) : Boolean{
        var passError: String? = null
        val password = binding.confirmPassEditText.text.toString()
        if(password.isEmpty()){
            passError = "Confirm password is required"
        }
        else if(password.length <= 6){
            passError = "Confirm password must be 6 characters long"
        }

        if(passError != null && shouldUpdateView){
            binding.confirmPassContainer.apply {
                isErrorEnabled = true;
                error = passError
            }
        }

        return passError == null
    }
    private fun validateConfirmAndPass(shouldUpdateView: Boolean = true) : Boolean{
        var passError : String? = null
        val password = binding.passEditText.text.toString()
        val confirmPass = binding.confirmPassEditText.text.toString()
        if(password != confirmPass){
            passError = "Confirm password doesn't match with password"
        }
        if(passError != null && shouldUpdateView){
            binding.confirmPassContainer.apply {
                isErrorEnabled = true;
                error = passError
            }
        }
        return passError == null
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
                        if(validatePassword() && binding.confirmPassEditText.text!!.isNotEmpty() && validateConfirmPassword() && validateConfirmAndPass()){
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
                        if(validateConfirmPassword() && validatePassword() && validateConfirmAndPass()){
                            if(binding.passContainer.isErrorEnabled){
                                binding.passContainer.isErrorEnabled = false
                            }
                        }
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

    private fun onSubmit() {
        if(validate()){
            val regisBody = registerBody(binding.nameEditText.text.toString(),binding.emailEditText.text.toString(),"hanoi",binding.phoneEditText.text.toString(),binding.passEditText.text.toString())
            Toast.makeText(requireContext(),regisBody.toString(),Toast.LENGTH_LONG).show()
            authViewModel.makeRegister(regisBody)
            authViewModel.user.observe(viewLifecycleOwner){
                when(it){
                    is Resource.Success -> {
                        it.data.let { UserResponse->
                            val loginUser = User(UserResponse?.user!!.__v,UserResponse?.user!!._id,UserResponse?.user!!.address,UserResponse?.user!!.email,UserResponse?.user!!.password,UserResponse?.user!!.phoneNumber,UserResponse?.user!!.username)
                            sharePreferenceUtils.saveToken(UserResponse?.token.toString(),requireContext())
                            sharePreferenceUtils.saveUser(loginUser,requireContext())
                            findNavController().navigate(R.id.action_signUpScreen_to_mainActivity)
                        }
                    }
                    is Resource.Error -> {
                        when(it.message){
                            "401" -> {
                                val dialog = layoutInflater.inflate(R.layout.custom_dialog,null)
                                val myDialog = Dialog(requireContext())
                                myDialog.setContentView(dialog)
                                myDialog.setCancelable(true)
                                myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                                myDialog.show()
                                val subBtn = dialog.findViewById<Button>(R.id.submitButton)
                                subBtn.setOnClickListener {
                                    myDialog.dismiss()
                                }
                                Toast.makeText(requireContext(),"Incorrect email or password",Toast.LENGTH_LONG).show()
                            }
                            "404" -> {
                                Toast.makeText(requireContext(),"error",Toast.LENGTH_LONG).show()
                            }

                        }
                    }
                    is Resource.Loading -> {
                        Toast.makeText(requireContext(),"Loading",Toast.LENGTH_LONG).show()
                    }

                }
            }
        }
    }

    private fun validate() : Boolean{
        var isValid = true;
        if(!validateEmail()) isValid = false
        if(!validatePassword()) isValid = false
        if(!validateNumber()) isValid= false
        if(!validateConfirmAndPass()) isValid = false
        if(!validateConfirmPassword()) isValid = false
        return isValid;
    }


}