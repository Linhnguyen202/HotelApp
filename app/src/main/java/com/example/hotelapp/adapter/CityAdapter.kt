package com.example.hotelapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelapp.R
import com.example.hotelapp.model.City

class CityAdapter : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    inner class CityViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private var titleBig : TextView = view.findViewById(R.id.cityTitle)

        public fun init(city: City){
            titleBig.text = city.name


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        return CityViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.city_box_layout,parent,false)
        )
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val value =differ.currentList[position]
        holder.init(value)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    private val differCallback = object : DiffUtil.ItemCallback<City>(){
        override fun areItemsTheSame(oldItem: City, newItem: City): Boolean {
            return oldItem.id != newItem.id

        }

        override fun areContentsTheSame(oldItem: City, newItem: City): Boolean {
            return oldItem.id != newItem.id
        }

    }
    val differ = AsyncListDiffer(this,differCallback)
}