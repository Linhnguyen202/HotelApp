package com.example.hotelapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hotelapp.R
import com.example.hotelapp.model.Hotel

class PopularHomeAdapter(var onClick : (Hotel)->Unit,var onClickFavorButton: (Hotel) -> Unit, var onClickRemoveFavorButton : (Hotel) -> Unit ) : RecyclerView.Adapter<PopularHomeAdapter.PopularHomeViewModel>() {
    inner class PopularHomeViewModel(view: View) : RecyclerView.ViewHolder(view){
        private var titleHotelNor : TextView = view.findViewById(R.id.titleHotelNor)
        private var priceHotelNor : TextView = view.findViewById(R.id.priceHotelNor)
        private var locationTitleNor : TextView = view.findViewById(R.id.locationTitleNor)
        private var textValueNor : TextView = view.findViewById(R.id.locationTitleNor)
        private var ratingBar : RatingBar = view.findViewById(R.id.ratingBarNor)
        private var normalLayout : ConstraintLayout = view.findViewById(R.id.normalLayout)
        private var likeButton : CheckBox = view.findViewById(R.id.likeButton)
        private var bannerHotel : ImageView = view.findViewById(R.id.bannerHotel)
        public fun init(hotel: Hotel){
            titleHotelNor.text = hotel.name
            priceHotelNor.text = hotel.price.toString()
            locationTitleNor.text = hotel.address
            ratingBar.rating = hotel.rate.toFloat()
            normalLayout.setOnClickListener {
                onClick.invoke(hotel)
            }

            Glide.with(this.itemView).load(hotel.image).into(bannerHotel)

            likeButton.setOnCheckedChangeListener { checkbox, isChecked ->
                if(isChecked){
                    onClickFavorButton.invoke(hotel)
                }
                else{
                    onClickRemoveFavorButton.invoke(hotel)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularHomeViewModel {
        return PopularHomeViewModel(
            LayoutInflater.from(parent.context).inflate(R.layout.hotel_list_normal,parent,false)
        )
    }

    override fun onBindViewHolder(holder: PopularHomeViewModel, position: Int) {
        val value= differ.currentList[position]
        holder.init(value)
    }

    override fun getItemCount(): Int {
        if(differ.currentList.size > 3 ){
            return 4;
        }
        else{
            return differ.currentList.size
        }

    }
    private val differCallback = object : DiffUtil.ItemCallback<Hotel>(){
        override fun areItemsTheSame(oldItem: Hotel, newItem: Hotel): Boolean {
            return oldItem._id == newItem._id

        }

        override fun areContentsTheSame(oldItem: Hotel, newItem: Hotel): Boolean {
            return oldItem._id == newItem._id
        }

    }
    val differ = AsyncListDiffer(this,differCallback)
}