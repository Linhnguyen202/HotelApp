package com.example.hotelapp.ui.bookingPage

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.hotelapp.adapter.ChatAdapter
import com.example.hotelapp.application.HotelApplication
import com.example.hotelapp.database.HotelDatabase
import com.example.hotelapp.databinding.FragmentChatScreenBinding
import com.example.hotelapp.model.*
import com.example.hotelapp.repository.ChatRepository
import com.example.hotelapp.share.sharePreferenceUtils
import com.example.hotelapp.socket.SocketHandler
import com.example.hotelapp.utils.Resource
import com.example.hotelapp.viewModel.ChatViewModel.ChatViewModel
import com.example.hotelapp.viewModel.ChatViewModel.ChatViewModelProviderFactory


class ChatScreen : Fragment() {
    lateinit var binding : FragmentChatScreenBinding
    private lateinit var socketHandler: SocketHandler
    private lateinit var chatAdapter: ChatAdapter
    private val chatList = mutableListOf<MessageResponse>()
    val args : ChatScreenArgs by navArgs()
   var CHAT_ID : String? = null
    private val repository by lazy {
        ChatRepository(HotelDatabase(requireContext()));
    }
    private val viewModelProviderFactory by lazy {
        ChatViewModelProviderFactory(HotelApplication(),repository)
    }

    private val chatViewModel by lazy {
        ViewModelProvider(this,viewModelProviderFactory)[ChatViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createChat()
        socketHandler = SocketHandler()
        chatAdapter = ChatAdapter(args.user.toString())
        binding.rvChat.apply{
            adapter = chatAdapter
        }

        makeChat()
        checkKeyboradOpen(view)
    }

    private fun createChat() {
        val token = "Bearer "+sharePreferenceUtils.getToken(requireContext()).toString()
        val userId = args.user.toString()
        val hotelId = args.hotel.toString()
        val chatBody = ChatBody(userId, hotelId)
        chatViewModel.makeChat(token,chatBody)
        chatViewModel.userChat.observe(viewLifecycleOwner){
            when(it) {
                is Resource.Success -> {
                    it.data.let { ChatResponse ->
                        CHAT_ID = ChatResponse?._id.toString()
                        getChat()
                    }
                }
                is Resource.Error ->{

                }
                is Resource.Loading ->{
//                    Toast.makeText(requireContext(),"Loading",Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    private fun getChat(){
        val token = "Bearer "+sharePreferenceUtils.getToken(requireContext()).toString()
        chatViewModel.getListMessage(token,CHAT_ID.toString())
        chatViewModel.listMessage.observe(viewLifecycleOwner){
            when(it) {
                is Resource.Success -> {
                    it.data.let { ChatListResponse ->
                        chatList.addAll(ChatListResponse!!.message)
                        chatAdapter.submitChat(chatList)
                        binding.rvChat.scrollToPosition(chatList.size - 1)
                    }
                }
                is Resource.Error ->{

                }
                is Resource.Loading ->{
                    Toast.makeText(requireContext(),"Loading",Toast.LENGTH_LONG).show()
                }
            }
        }

        socketHandler.onNewChat.observe(viewLifecycleOwner) {
            chatList.add(it)
            chatAdapter.submitChat(chatList)
            binding.rvChat.scrollToPosition(chatList.size - 1)
        }
    }
    private fun makeChat() {
        val token = "Bearer "+sharePreferenceUtils.getToken(requireContext()).toString()
        binding.btnSend.setOnClickListener{
            val message = binding.etMsg.text.toString()
            if(message.isNotEmpty()){
               val messageBody = MessageBody(
                   CHAT_ID.toString(),
                   args.user,
                   message
               )
                chatViewModel.sendMessage(token,messageBody)
                binding.etMsg.text?.clear()
            }
        }
        chatViewModel.message.observe(viewLifecycleOwner){
            when(it) {
                is Resource.Success -> {
                    it.data.let { MessageResponse ->
                        socketHandler.sendChat(MessageResponse!!)
                    }
                }
                is Resource.Error ->{

                }
                is Resource.Loading ->{

                }
            }
        }

    }
    private fun checkKeyboradOpen(view: View){
        view.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {

            override fun onGlobalLayout() {
                val rect = android.graphics.Rect()
                view.getWindowVisibleDisplayFrame(rect)
                val screenHeight = view.height
                binding.rvChat.scrollToPosition(chatList.size - 1)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        socketHandler.disconnection()
    }





}