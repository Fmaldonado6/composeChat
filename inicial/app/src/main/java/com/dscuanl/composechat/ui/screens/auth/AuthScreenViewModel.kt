package com.dscuanl.composechat.ui.screens.auth

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dscuanl.composechat.data.repositories.AuthRepository
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthCredential
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface AuthUiState {
    object Success : AuthUiState
    object Loading : AuthUiState
    object Initial : AuthUiState
    object Error : AuthUiState
}

class AuthScreenViewModel : ViewModel() {

    private val _uiState: MutableStateFlow<AuthUiState> = MutableStateFlow(AuthUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        checkSession()
    }

    private fun checkSession() {

    }

    fun signIn(credential: GoogleAuthCredential) {

    }

    fun changeState(state: AuthUiState) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.emit(state)
        }
    }

}