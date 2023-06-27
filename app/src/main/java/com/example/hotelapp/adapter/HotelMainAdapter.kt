package com.example.hotelapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hotelapp.MainActivity
import com.example.hotelapp.R
import com.example.hotelapp.model.Hotel

class HotelMainAdapter(val onClickItem : (Hotel) -> Unit, val onClickFavorButton: (Hotel)->Unit, val onClickRemoveFavorButton: (Hotel)->Unit,val checkFavorHotel : (Hotel) -> Boolean) : RecyclerView.Adapter<HotelMainAdapter.HotelMainViewModel>() {

    inner class HotelMainViewModel(view: View) : RecyclerView.ViewHolder(view){
        private var titleCardTxt : TextView = view.findViewById(R.id.titleCardTxt)
        private var locationCardTxt : TextView = view.findViewById(R.id.locationCardTxt)
        private var rateNumCardTxt : TextView = view.findViewById(R.id.rateNumCardTxt)
        private var hotelItemCard : CardView = view.findViewById(R.id.hotelItemCard)
        private var likeButton : CheckBox = view.findViewById(R.id.likeButton)
        private var thumbnailCard : ImageView = view.findViewById(R.id.thumbnailCard)
        public fun init(hotel: Hotel){
            titleCardTxt.text = hotel.name
            locationCardTxt.text = hotel.address.toString()
            rateNumCardTxt.text = hotel.rate.toString()
            hotelItemCard.setOnClickListener{
                onClickItem.invoke(hotel)
            }
            Glide.with(this.itemView).load(hotel.image).into(thumbnailCard)
            likeButton.setOnCheckedChangeListener{ checkbox, isChecked ->
                if(isChecked){
                    onClickFavorButton.invoke(hotel)
                }
                else{
                    onClickRemoveFavorButton.invoke(hotel)
                }
            }

            if(checkFavorHotel.invoke(hotel)){
                likeButton.isChecked = true
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelMainViewModel {
        return HotelMainViewModel(
            LayoutInflater.from(parent.context).inflate(R.layout.hotel_item_main,parent,false)
        )
    }

    override fun onBindViewHolder(holder: HotelMainViewModel, position: Int) {
        val value =differ.currentList[position]
        holder.init(value)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    private val differCallback = object : DiffUtil.ItemCallback<Hotel>(){
        override fun areItemsTheSame(oldItem: Hotel, newItem: Hotel): Boolean {
            return oldItem._id == newItem._id

        }

        override fun areContentsTheSame(oldItem: Hotel, newItem: Hotel): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this,differCallback)
}