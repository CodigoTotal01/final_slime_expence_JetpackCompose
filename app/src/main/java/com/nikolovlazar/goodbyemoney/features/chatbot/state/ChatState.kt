package com.nikolovlazar.goodbyemoney.features.chatbot.state

import android.graphics.Bitmap
import com.nikolovlazar.goodbyemoney.features.chatbot.data.Chat

data class ChatState (
    val chatList: MutableList<Chat> = mutableListOf(),
    val prompt: String = "",
    val bitmap: Bitmap? = null
)