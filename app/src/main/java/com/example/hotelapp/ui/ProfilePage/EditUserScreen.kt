package com.example.hotelapp.ui.ProfilePage

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Patterns
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hotelapp.MainActivity
import com.example.hotelapp.R
import com.example.hotelapp.databinding.FragmentEditUserScreenBinding
import com.example.hotelapp.model.User
import com.example.hotelapp.model.registerBody
import com.example.hotelapp.share.sharePreferenceUtils
import com.example.hotelapp.utils.Resource
import com.example.hotelapp.viewModel.AuthViewModel.AuthViewModel


class EditUserScreen : Fragment(), View.OnClickListener,View.OnFocusChangeListener, View.OnKeyListener {
    lateinit var binding : FragmentEditUserScreenBinding
    private val authViewModel by lazy {
        ViewModelProvider(requireActivity(),(activity as MainActivity).authViewModelProviderFactory)[AuthViewModel::class.java]
    }
    val user by lazy {
        sharePreferenceUtils.getUser(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditUserScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nameEditText.onFocusChangeListener = this
        binding.phoneEditText.onFocusChangeListener  = this
        binding.emailEditText.onFocusChangeListener = this
        binding.submitProfileBtn.setOnClickListener(this)
        setData()
        addEvent()

    }

    private fun addEvent() {
        binding.headerToolbar.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setData() {
        binding.emailEditText.setText(user.email.toString())
        binding.phoneEditText.setText(user.phoneNumber.toString())
        binding.nameEditText.setText(user.username.toString())
        binding.headerToolbar.titleToolbar.setText("My Profle")
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

    override fun onClick(view: View?) {
        if(view != null && view.id == R.id.submitProfileBtn){
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
            }
        }
    }

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
//        if(KeyEvent.KEYCODE_ENTER == keyCode && event!!.action == KeyEvent.ACTION_UP){
//            onSubmit()
//        }
        return false
    }

    private fun onSubmit() {
        if(validate()){
            val newUser = User(user.__v, user._id, user.address, binding.emailEditText.text.toString(),user.password,binding.phoneEditText.text.toString(),binding.nameEditText.text.toString())
            authViewModel.updateUserProfile(newUser,sharePreferenceUtils.getUser(requireContext())._id)
            authViewModel.userUdapteProfile.observe(viewLifecycleOwner){
                when(it){
                    is Resource.Success -> {
                        it.data.let { UserResponse->
                            val loginUser = User(UserResponse?.user!!.__v,UserResponse?.user!!._id,UserResponse?.user!!.address,UserResponse?.user!!.email,UserResponse?.user!!.password,UserResponse?.user!!.phoneNumber,UserResponse?.user!!.username)
                            sharePreferenceUtils.saveUser(loginUser,requireContext())
                            findNavController().popBackStack()
                        }
                    }
                    is Resource.Error -> {

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
        if(!validateNumber()) isValid= false
        return isValid;
    }
}