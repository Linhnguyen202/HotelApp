package com.example.hotelapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hotelapp.R
import com.example.hotelapp.model.Hotel

class  HotelMainAdapter(
    val onClickItem: (Hotel) -> Unit, val onClickFavorButton: (Hotel,Boolean) -> Boolean, val checkFavorHotel: (Hotel) -> Boolean) : PagingDataAdapter<Hotel,HotelMainAdapter.HotelMainViewModel>(differCallback) {

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
                likeButton.isChecked = onClickFavorButton.invoke(hotel,isChecked)
            }

//            if(checkFavorHotel.invoke(hotel)){
//                likeButton.isChecked = true
//            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelMainViewModel {
        return HotelMainViewModel(
            LayoutInflater.from(parent.context).inflate(R.layout.hotel_item_main,parent,false)
        )
    }

    override fun onBindViewHolder(holder: HotelMainViewModel, position: Int) {
        val value = getItem(position)
        if(value != null){
            holder.init(value)
            holder.setIsRecyclable(false)
        }


    }

   companion object {
       private val differCallback = object : DiffUtil.ItemCallback<Hotel>(){
           override fun areItemsTheSame(oldItem: Hotel, newItem: Hotel): Boolean {
               return oldItem._id == newItem._id

           }

           override fun areContentsTheSame(oldItem: Hotel, newItem: Hotel): Boolean {
               return oldItem == newItem
           }

       }
   }

}