package com.dscuanl.composechat.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.dscuanl.composechat.data.models.Message
import com.dscuanl.composechat.data.network.AuthService
import com.dscuanl.composechat.data.repositories.AuthRepository
import com.dscuanl.composechat.data.repositories.ChatRepository
import com.dscuanl.composechat.ui.screens.auth.AuthUiState
import kotlinx.coroutines.flow.*

sealed interface HomeUiState {
    object Loading : HomeUiState
    object Loaded : HomeUiState
    object Empty : HomeUiState
    object Error : HomeUiState
}

class HomeViewModel : ViewModel() {

    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState.Loaded)
    val uiState = _uiState.asStateFlow()

    var messageState by mutableStateOf("")
    val messages = ChatRepository.messages

    fun sendMessage() {
        val currentUser = AuthService.currentUser.value ?: return
        val trimmedMessage = messageState.trim()
        if (trimmedMessage.isEmpty()) return

        val newMessage = Message(
            author = currentUser.displayName,
            message = trimmedMessage,
            authorId = currentUser.id,
            authorPicture = currentUser.photoUrl
        )
        messageState = ""
        ChatRepository.sendMessage(message = newMessage)
    }


    fun onMessageTyped(value: String) {
        messageState = value
    }


}