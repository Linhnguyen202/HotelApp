package com.example.hotelapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hotelapp.R
import com.example.hotelapp.model.Hotel

class HotelBigAdapter(var onClick : (Hotel)->Unit,var onClickFavorButton: (Hotel) -> Unit, var onClickRemoveFavorButton : (Hotel) -> Unit ) : RecyclerView.Adapter<HotelBigAdapter.HotelBigViewModel>() {

    inner class HotelBigViewModel(view: View) : RecyclerView.ViewHolder(view){
        private var titleBig : TextView = view.findViewById(R.id.hotelNameBig)
        private var priceBig : TextView = view.findViewById(R.id.priceHotelBig)
        private var startRateBig : TextView = view.findViewById(R.id.value_rateBig)
        private var mainContainer : ConstraintLayout = view.findViewById(R.id.mainContainer)
        private var likeButton : CheckBox = view.findViewById(R.id.likeButton)
        private var bannerCardBig : ImageView= view.findViewById(R.id.bannerCardBig)
        public fun init(hotel: Hotel){
            titleBig.text = hotel.name
            priceBig.text = hotel.price.toString()
            startRateBig.text = hotel.rate.toString()
            mainContainer.setOnClickListener {
                onClick.invoke(hotel)
            }
            Glide.with(this.itemView).load(hotel.image).into(bannerCardBig)
            likeButton.setOnCheckedChangeListener{ checkbox, isChecked ->
                if(isChecked){
                    onClickFavorButton.invoke(hotel)
                }
                else{
                    onClickRemoveFavorButton.invoke(hotel)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelBigViewModel {
        return HotelBigViewModel(
            LayoutInflater.from(parent.context).inflate(R.layout.big_item,parent,false)
        )
    }

    override fun onBindViewHolder(holder: HotelBigViewModel, position: Int) {
       val value =differ.currentList[position]
        holder.init(value)
    }

    override fun getItemCount(): Int {
        if(differ.currentList.size > 3){
            return 4;
        }
        else{
            return differ.currentList.size
        }

    }
    private val differCallback = object :DiffUtil.ItemCallback<Hotel>(){
        override fun areItemsTheSame(oldItem: Hotel, newItem: Hotel): Boolean {
            return oldItem._id == newItem._id

        }

        override fun areContentsTheSame(oldItem: Hotel, newItem: Hotel): Boolean {
            return oldItem._id == newItem._id
        }

    }
    val differ = AsyncListDiffer(this,differCallback)
}