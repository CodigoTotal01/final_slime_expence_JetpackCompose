package com.nikolovlazar.goodbyemoney.features.chatbot.data

import android.graphics.Bitmap

data class Chat (
    val prompt: String,
    val bitmap: Bitmap?,
    val isFromUser: Boolean,
    val isLoading: Boolean = false

)