package com.example.hotelapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelapp.R
import com.example.hotelapp.model.Category
import com.example.hotelapp.model.RoomType

class RoomTypeAdapter : RecyclerView.Adapter<RoomTypeAdapter.RoomTypeViewHolder>() {
    inner class RoomTypeViewHolder(view : View) : RecyclerView.ViewHolder(view){
        public fun init(roomType: RoomType){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomTypeViewHolder {
        return RoomTypeViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.room_type_item,parent,false)
        )
    }

    override fun onBindViewHolder(holder: RoomTypeViewHolder, position: Int) {
        val value = differ.currentList[position]
        holder.init(value)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    private val differCallback = object : DiffUtil.ItemCallback<RoomType>(){
        override fun areItemsTheSame(oldItem: RoomType, newItem: RoomType): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RoomType, newItem: RoomType): Boolean {
            return oldItem.id == newItem.id
        }

    }
    val differ = AsyncListDiffer(this,differCallback)
}