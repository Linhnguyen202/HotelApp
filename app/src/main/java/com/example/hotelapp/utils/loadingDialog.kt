package com.example.hotelapp.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.compose.ui.graphics.Color
import com.example.hotelapp.R

class loadingDialog(var context: Context) {
    lateinit var dialog: Dialog
    public fun startLoading(){
        dialog = Dialog(context)
        dialog.setContentView(R.layout.loading_item)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        dialog.show()
    }
    public fun endLoading(){
        dialog.dismiss()
    }
}