package com.example.hotelapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelapp.R
import com.example.hotelapp.model.Room

class RoomAdapter(val onClick : (Room)->Unit) : RecyclerView.Adapter<RoomAdapter.RoomViewModel>() {
    inner class RoomViewModel(view: View) : RecyclerView.ViewHolder(view){
        private var titleCardTxt : TextView = view.findViewById(R.id.titleCardTxt)
        private var roomNumberTxt : TextView = view.findViewById(R.id.roomNumberTxt)
        private var priceNumberTxt : TextView = view.findViewById(R.id.priceCardText)
        private var selectBtn : CardView = view.findViewById(R.id.selectBtn)
        public fun init(room: Room){
            titleCardTxt.text = room.name
            roomNumberTxt.text = "${room.numberRoom} allowed"
            priceNumberTxt.text = room.price.toString()
            selectBtn.setOnClickListener{
                onClick.invoke(room)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewModel {
        return RoomViewModel(
            LayoutInflater.from(parent.context).inflate(R.layout.room_item_main,parent,false)
        )
    }

    override fun onBindViewHolder(holder: RoomViewModel, position: Int) {
        val value =differ.currentList[position]
        holder.init(value)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    private val differCallback = object : DiffUtil.ItemCallback<Room>(){
        override fun areItemsTheSame(oldItem: Room, newItem: Room): Boolean {
            return oldItem._id == newItem._id

        }

        override fun areContentsTheSame(oldItem: Room, newItem: Room): Boolean {
            return oldItem._id == newItem._id
        }

    }
    val differ = AsyncListDiffer(this,differCallback)
}