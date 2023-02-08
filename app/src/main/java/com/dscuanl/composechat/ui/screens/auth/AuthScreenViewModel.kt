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
        viewModelScope.launch(Dispatchers.IO) {
            if (AuthRepository.checkSession())
                _uiState.emit(AuthUiState.Success)
            else
                _uiState.emit(AuthUiState.Initial)
        }
    }

    fun signIn(credential: AuthCredential) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _uiState.emit(AuthUiState.Loading)
                AuthRepository.signInWithGoogle(credential)
                _uiState.emit(AuthUiState.Success)
            } catch (e: java.lang.Exception) {
                Log.e("Error", "e", e)
                _uiState.emit(AuthUiState.Error)
            }
        }
    }

    fun changeState(state: AuthUiState) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.emit(state)
        }
    }

}