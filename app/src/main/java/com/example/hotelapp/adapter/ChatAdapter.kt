package com.example.hotelapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelapp.databinding.ItemChatSelfBinding
import com.example.hotelapp.databinding.ItemChatSelfOtherBinding
import com.example.hotelapp.model.Chat
import com.example.hotelapp.model.MessageBody
import com.example.hotelapp.model.MessageResponse
import com.example.hotelapp.share.sharePreferenceUtils

class ChatAdapter(val userId : String): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM_SELF = 1
    private val ITEM_OTHER = 2

    private val diffcallback = object : DiffUtil.ItemCallback<MessageResponse>() {
        override fun areItemsTheSame(oldItem: MessageResponse, newItem: MessageResponse): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MessageResponse, newItem: MessageResponse): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffcallback)

    fun submitChat(chats: List<MessageResponse>) {
        differ.submitList(chats)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_SELF) {
            val binding = ItemChatSelfBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            SelfChatItemViewHolder(binding)
        } else {
            val binding = ItemChatSelfOtherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            OtherChatItemViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chat = differ.currentList[position]
        if (chat.senderId.toString().equals(userId)) {
            ( holder as SelfChatItemViewHolder).bind(chat)
        } else {
            (holder as OtherChatItemViewHolder).bind(chat)
        }
    }


    inner class OtherChatItemViewHolder(val binding: ItemChatSelfOtherBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(messageBody: MessageResponse) {
            binding.apply {
                name.text = messageBody.senderId
                msg.text = messageBody.text
            }
        }
    }

    inner class SelfChatItemViewHolder(val binding: ItemChatSelfBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(messageBody: MessageResponse) {
            binding.apply {
                name.text = "You"
                msg.text = messageBody.text
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val chat = differ.currentList[position]
        return if (chat.senderId.toString().equals(userId)) ITEM_SELF else ITEM_OTHER
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}