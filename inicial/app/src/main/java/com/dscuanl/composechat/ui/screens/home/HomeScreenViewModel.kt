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


    fun sendMessage() {

    }

    fun signOut() {

    }

    fun onMessageTyped(value: String) {
        messageState = value
    }


}