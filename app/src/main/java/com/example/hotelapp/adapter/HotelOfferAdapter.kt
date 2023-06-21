package com.example.hotelapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelapp.R
import com.example.hotelapp.model.HotelOffer

class HotelOfferAdapter : RecyclerView.Adapter<HotelOfferAdapter.HotelOfferViewHodel>() {

    inner class HotelOfferViewHodel(view: View) : RecyclerView.ViewHolder(view){
        private val titleOffer : TextView = view.findViewById(R.id.titleOffer)

        public fun init(hotelOffer: HotelOffer){
            titleOffer.text = hotelOffer.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelOfferViewHodel {
        return HotelOfferViewHodel(
            LayoutInflater.from(parent.context).inflate(R.layout.hotel_offer_item,parent,false)
        )
    }

    override fun onBindViewHolder(holder: HotelOfferViewHodel, position: Int) {
        val value =differ.currentList[position]
        holder.init(value)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    private val differCallback = object : DiffUtil.ItemCallback<HotelOffer>(){
        override fun areItemsTheSame(oldItem: HotelOffer, newItem: HotelOffer): Boolean {
            return oldItem.id == newItem.id

        }

        override fun areContentsTheSame(oldItem: HotelOffer, newItem: HotelOffer): Boolean {
            return oldItem.id == newItem.id
        }

    }
    val differ = AsyncListDiffer(this,differCallback)
}