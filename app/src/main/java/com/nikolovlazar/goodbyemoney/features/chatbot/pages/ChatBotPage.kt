package com.nikolovlazar.goodbyemoney.features.chatbot.pages

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddPhotoAlternate
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.nikolovlazar.goodbyemoney.features.chatbot.utils.ChatUiEvent
import com.nikolovlazar.goodbyemoney.features.chatbot.viewModel.ChatViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

private val uriState = MutableStateFlow("")


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen() {
    val chaViewModel = viewModel<ChatViewModel>()
    val chatState = chaViewModel.chatState.collectAsState().value

    val bitmap = getBitmap()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            reverseLayout = true
        ) {
            itemsIndexed(chatState.chatList) { index, chat ->
                if (chat.isFromUser) {
                    UserChatItem(
                        prompt = chat.prompt, bitmap = chat.bitmap
                    )
                } else {
                    ModelChatItem(response = chat.prompt)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {
                bitmap?.let {
                    Image(
                        modifier = Modifier
                            .size(40.dp)
                            .padding(bottom = 2.dp)
                            .clip(RoundedCornerShape(6.dp)),
                        contentDescription = "picked image",
                        contentScale = ContentScale.Crop,
                        bitmap = it.asImageBitmap()
                    )
                }

                Icon(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            // Implementa el selector de imágenes aquí
                        },
                    imageVector = Icons.Rounded.AddPhotoAlternate,
                    contentDescription = "Add Photo",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            TextField(
                modifier = Modifier
                    .weight(1f),
                value = chatState.prompt,
                onValueChange = {
                    chaViewModel.onEvent(ChatUiEvent.UpdatePrompt(it))
                },
                placeholder = {
                    Text(text = "Type a prompt")
                }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        chaViewModel.onEvent(ChatUiEvent.SendPrompt(chatState.prompt, bitmap))
                        uriState.update { "" }
                    },
                imageVector = Icons.Rounded.Send,
                contentDescription = "Send prompt",
                tint = MaterialTheme.colorScheme.primary
            )

        }

    }
}

@Composable
fun UserChatItem(prompt: String, bitmap: Bitmap?) {
    Column(
        modifier = Modifier.padding(start = 100.dp, bottom = 16.dp)
    ) {

        bitmap?.let {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .padding(bottom = 2.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentDescription = "image",
                contentScale = ContentScale.Crop,
                bitmap = it.asImageBitmap()
            )
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp),
            text = prompt,
            fontSize = 17.sp,
            color = MaterialTheme.colorScheme.onPrimary
        )

    }
}

@Composable
fun ModelChatItem(response: String) {
    Column(
        modifier = Modifier.padding(end = 100.dp, bottom = 16.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Cyan)
                .padding(16.dp),
            text = response,
            fontSize = 17.sp,
            color = MaterialTheme.colorScheme.onPrimary
        )

    }
}

@Composable
private fun getBitmap(): Bitmap? {
    val uri = uriState.collectAsState().value

    val imageState: AsyncImagePainter.State = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(uri)
            .size(Size.ORIGINAL)
            .build()
    ).state

    if (imageState is AsyncImagePainter.State.Success) {
        return imageState.result.drawable.toBitmap()
    }

    return null
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatBotPage (
    navController: NavController,
){
    Scaffold(
//        topBar = {
//            TopAppBar(title = { Text("CHATBOT") })
//        }
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            ChatScreen()
            }
    }
}

