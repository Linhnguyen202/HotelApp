package com.example.hotelapp.ui.start

import android.app.Dialog
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Patterns
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hotelapp.R
import com.example.hotelapp.application.HotelApplication
import com.example.hotelapp.database.HotelDatabase
import com.example.hotelapp.databinding.FragmentLogInScreenBinding
import com.example.hotelapp.model.SignInBody
import com.example.hotelapp.model.User
import com.example.hotelapp.repository.AuthRepository
import com.example.hotelapp.share.sharePreferenceUtils
import com.example.hotelapp.utils.Resource
import com.example.hotelapp.utils.loadingDialog
import com.example.hotelapp.viewModel.AuthViewModel.AuthViewModel
import com.example.hotelapp.viewModel.AuthViewModel.AuthViewModelProviderFactory


class LogInScreen : Fragment(), View.OnClickListener,View.OnFocusChangeListener, View.OnKeyListener{
    lateinit var binding : FragmentLogInScreenBinding
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
        binding = FragmentLogInScreenBinding.inflate(layoutInflater)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.emailEditText.onFocusChangeListener = this
        binding.passEditText.onFocusChangeListener  = this
        binding.sigInButton.setOnClickListener(this)
        binding.guest.paintFlags = binding.guest.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG

        addEvent()

    }

    private fun addEvent() {
        binding.guest.setOnClickListener{
            findNavController().navigate(R.id.action_logInScreen_to_mainActivity)
        }
        binding.signUpTxt.setOnClickListener{
            findNavController().navigate(R.id.action_logInScreen_to_signUpScreen)
        }
    }


    private fun  validateEmail() : Boolean{
        var emailError: String? = null
        val email = binding.emailEditText.text.toString()
        if(email.isEmpty()){
            emailError = "Email is required"
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailError = "Email address is invalid"
        }
        if(emailError != null){
            Toast.makeText(requireContext(),emailError,Toast.LENGTH_SHORT).show()
            binding.emailContainer.apply {
                isErrorEnabled = true;
                error = emailError
            }
        }

        return emailError == null
    }
    private fun validatePassword() : Boolean{
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


    override fun onClick(v: View?) {
        if(v != null && v.id == R.id.sigInButton ){
            onSubmit()
        }
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if(view != null){
            when(view.id){
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
                        validatePassword()
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
    private fun onSubmit(){
        if(validate()){
            val signInBody = SignInBody(binding.emailEditText.text.toString(),binding.passEditText.text.toString())
            authViewModel.makeLogin(signInBody)
            authViewModel.user.observe(viewLifecycleOwner){
                when(it){
                    is Resource.Success -> {
                        it.data.let { UserResponse->
                            val loginUser = User(UserResponse?.user!!.__v,UserResponse?.user!!._id,UserResponse?.user!!.address,UserResponse?.user!!.email,UserResponse?.user!!.password,UserResponse?.user!!.phoneNumber,UserResponse?.user!!.username)
                            sharePreferenceUtils.saveToken(UserResponse?.token.toString(),requireContext())
                            sharePreferenceUtils.saveUser(loginUser,requireContext())
                            loadingDialog.endLoading(requireContext())
                            findNavController().navigate(R.id.action_logInScreen_to_mainActivity)

                        }
                    }
                    is Resource.Error -> {
                        when(it.message){
                            "401" -> {
                                loadingDialog.endLoading(requireContext())
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
                            }
                            "404" -> {
                                loadingDialog.endLoading(requireContext())
                                Toast.makeText(requireContext(),"error",Toast.LENGTH_LONG).show()
                            }

                        }
                    }
                    is Resource.Loading -> {
                        loadingDialog.startLoading(requireContext())
                    }

                }
            }
        }
    }
    private fun validate() : Boolean{
        var isValid = true;
        if(!validateEmail()) isValid = false
        if(!validatePassword()) isValid = false
        return isValid;
    }


}