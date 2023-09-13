package com.example.hotelapp.ui.ProfilePage

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import com.example.hotelapp.MainActivity
import com.example.hotelapp.R
import com.example.hotelapp.databinding.FragmentLanguageSettingScreenBinding
import com.example.hotelapp.share.sharePreferenceUtils


class LanguageSettingScreen : Fragment() {
    lateinit var binding : FragmentLanguageSettingScreenBinding
    lateinit var codeLanguague : String;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLanguageSettingScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkLanguage(view)
        binding.chanpass.setOnClickListener {
            showDialog(view)
        }
        binding.languageContainer.setOnCheckedChangeListener{ group, checkId ->
            // on below line we are getting radio button from our group.
            val radioButton = view.findViewById<RadioButton>(checkId)
           if(radioButton.text.equals("English")){
               codeLanguague = "eng"
           }
            else if(radioButton.text.equals("Vietnamese")){
               codeLanguague = "vi"
           }
        }
    }

    private fun checkLanguage(view: View) {
        codeLanguague = sharePreferenceUtils.getLanguage(requireContext()).toString()
        val radioButton = view.findViewById<RadioButton>(R.id.eng_lan)
        val radioButton2 = view.findViewById<RadioButton>(R.id.viet_lan)
        Toast.makeText(requireContext(),codeLanguague,Toast.LENGTH_SHORT).show()
        if(sharePreferenceUtils.getLanguage(requireContext()).toString() == "eng"){
            radioButton.isChecked = true
            radioButton2.isChecked = false
        }
        else if(sharePreferenceUtils.getLanguage(requireContext()).toString() == "vi"){
            radioButton.isChecked = false
            radioButton2.isChecked = true
        }
    }
    private fun showDialog(view: View) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Alert")
            .setMessage("Are you sure to change your language?")
            .setCancelable(true)
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                (activity as MainActivity).setLocal(codeLanguague)
                sharePreferenceUtils.saveLanguage(codeLanguague, requireContext())
                activity?.recreate()
            })
            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
                checkLanguage(view)
            })
        builder.show()
    }



}