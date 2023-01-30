package com.dscuanl.composechat.ui.screens.home

import androidx.lifecycle.ViewModel
import com.dscuanl.composechat.data.repositories.AuthRepository
import com.dscuanl.composechat.ui.screens.auth.AuthUiState
import kotlinx.coroutines.flow.*

sealed interface HomeUiState {
    object Loading : HomeUiState
    object Loaded : HomeUiState
    object Empty : HomeUiState
    object Error : HomeUiState
}

class HomeViewModel : ViewModel() {

    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    val users = AuthRepository.users
        .retryWhen { cause, attempt ->
            emit(mutableListOf())
            true
        }
        .onEach {
            if (it.isEmpty()) _uiState.emit(HomeUiState.Empty)
            else _uiState.emit(HomeUiState.Loaded)
        }
        .catch { exception -> _uiState.emit(HomeUiState.Error) }


    fun retry() {
        users.retry()
    }
}