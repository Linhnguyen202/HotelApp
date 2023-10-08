package com.example.hotelapp.socket

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.hotelapp.model.Chat
import com.example.hotelapp.model.MessageBody
import com.example.hotelapp.model.MessageResponse
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

class SocketHandler {
    private var socket : Socket? = null
    private val _onNewChat = MutableLiveData<MessageResponse>()
    val onNewChat: MutableLiveData<MessageResponse> get() = _onNewChat
    init {
        try{
            socket = IO.socket(SOCKET_URL)
            socket?.connect()
            registerOnNewChat()
        }
        catch (e : URISyntaxException)
        {
            e.printStackTrace()
        }
    }
    private fun registerOnNewChat() {
        socket?.on(CHAT_KEYS.BROADCAST) { args->
            args?.let { d ->
                if (d.isNotEmpty()) {
                    val data = d[0]
                    if (data.toString().isNotEmpty()) {
                        val chat = Gson().fromJson(data.toString(), MessageResponse::class.java)
                        _onNewChat.postValue(chat)
                    }
                }

            }
        }
    }


    fun disconnection(){
        socket?.disconnect()
        socket?.off()
    }

    fun sendChat(messageResponse: MessageResponse){
        val jsonStr = Gson().toJson(messageResponse, MessageResponse::class.java)
        socket?.emit(CHAT_KEYS.NEW_MESSAGE, jsonStr)
    }
    private object CHAT_KEYS{
        const val  NEW_MESSAGE = "new_message"
        const val GET_MESSAGE = "get_message"
        const val BROADCAST = "broadcast"
    }
    companion object {
        const val SOCKET_URL = "http://192.168.3.102:3000/"
    }


}