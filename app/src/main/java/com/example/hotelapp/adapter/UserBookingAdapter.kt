package com.example.hotelapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hotelapp.R
import com.example.hotelapp.model.Booking

class UserBookingAdapter(var onClickItem : (Booking)->Unit) : RecyclerView.Adapter<UserBookingAdapter.BookingViewHolder>() {
    inner class BookingViewHolder(view : View) : RecyclerView.ViewHolder(view){
        private val bookingLayout : ConstraintLayout = view.findViewById(R.id.bookingLayout)
        private val bookingTitle: TextView = view.findViewById(R.id.titleCardTxt)
        private val priceCardText: TextView = view.findViewById(R.id.priceCardText)
        private val detailButton : CardView = view.findViewById(R.id.detailBtn)
        private val textButton : TextView = view.findViewById(R.id.cardTextBtn)
        private val thumbnailCard : ImageView = view.findViewById(R.id.thumbnailCard)
        public fun init(booking: Booking){
            when(booking.status){
                "Current"->{
                    textButton.text = "Detail"
                }
                "Pass" -> {
                    textButton.text = "Feedback"
                }
                "Pass" -> {
                    textButton.text = "Detail"
                }


            }
            bookingTitle.text = booking.hotel.name.toString()
            priceCardText.text = booking.totalPrice.toString()
            Glide.with(this.itemView).load(booking.hotel.image).into(thumbnailCard)
            detailButton.setOnClickListener{
                onClickItem.invoke(booking)
            }

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