package com.example.hotelapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelapp.R
import com.example.hotelapp.model.Booking

class BookingAdapter : RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {
    inner class BookingViewHolder(view : View) : RecyclerView.ViewHolder(view){
//        private val cateLayout : ConstraintLayout = view.findViewById(R.id.cateLayoutContent)
//        private val cateTitle: TextView = view.findViewById(R.id.cateTitle)
//        private val cateIcon: ImageView = view.findViewById(R.id.iconCate)
        public fun init(booking: Booking){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        return BookingViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.booking_item_main,parent,false)
        )
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val value = differ.currentList[position]
        holder.init(value)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    private val differCallback = object : DiffUtil.ItemCallback<Booking>(){
        override fun areItemsTheSame(oldItem: Booking, newItem: Booking): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: Booking, newItem: Booking): Boolean {
            return oldItem._id == newItem._id
        }

    }
    val differ = AsyncListDiffer(this,differCallback)
}