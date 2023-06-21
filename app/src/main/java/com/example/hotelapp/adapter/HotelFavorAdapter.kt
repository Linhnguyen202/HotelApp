package com.example.hotelapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelapp.R
import com.example.hotelapp.model.FavorHotel
import com.example.hotelapp.model.Hotel

class HotelFavorAdapter(val onClickItem : (Hotel) -> Unit, val onClickFavorButton: (Hotel)->Unit, val onClickRemoveFavorButton: (Hotel)->Unit) : RecyclerView.Adapter<HotelFavorAdapter.HotelMainViewModel>() {
    inner class HotelMainViewModel(view: View) : RecyclerView.ViewHolder(view){
        private var titleCardTxt : TextView = view.findViewById(R.id.titleCardTxt)
        private var locationCardTxt : TextView = view.findViewById(R.id.locationCardTxt)
        private var rateNumCardTxt : TextView = view.findViewById(R.id.rateNumCardTxt)
        private var hotelItemCard : CardView = view.findViewById(R.id.hotelItemCard)
        private var likeButton : CheckBox = view.findViewById(R.id.likeButton)
        public fun init(favorHotel: FavorHotel){
            titleCardTxt.text = favorHotel.hotel.name
            locationCardTxt.text = favorHotel.hotel.address.toString()
            rateNumCardTxt.text =favorHotel.hotel.rate.toString()
            hotelItemCard.setOnClickListener{
                onClickItem.invoke(favorHotel.hotel)
            }
            likeButton.isChecked = true
            likeButton.setOnCheckedChangeListener{ checkbox, isChecked ->
                if(isChecked){
                    onClickFavorButton.invoke(favorHotel.hotel)
                }
                else{
                    onClickRemoveFavorButton.invoke(favorHotel.hotel)
                }
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
    private val differCallback = object : DiffUtil.ItemCallback<FavorHotel>(){
        override fun areItemsTheSame(oldItem: FavorHotel, newItem: FavorHotel): Boolean {
            return oldItem._id == newItem._id

        }

        override fun areContentsTheSame(oldItem: FavorHotel, newItem: FavorHotel): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this,differCallback)
}