package com.dscuanl.composechat.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dscuanl.composechat.data.models.Message
import com.dscuanl.composechat.data.network.AuthService
import com.dscuanl.composechat.data.repositories.AuthRepository
import com.dscuanl.composechat.data.repositories.ChatRepository
import com.dscuanl.composechat.ui.screens.auth.AuthUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed interface HomeUiState {
    object Loading : HomeUiState
    object Loaded : HomeUiState
    object SignOut : HomeUiState
    object Empty : HomeUiState
    object Error : HomeUiState
}

class HomeViewModel : ViewModel() {

    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState.Loaded)
    val uiState = _uiState.asStateFlow()

    var messageState by mutableStateOf("")
    var dropDownExpanded by mutableStateOf(false)
    val messages = ChatRepository.messages
    val currentUser = AuthRepository.user

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

    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            AuthService.signOut()
            _uiState.emit(HomeUiState.SignOut)
        }
    }

    fun onMessageTyped(value: String) {
        messageState = value
    }


}