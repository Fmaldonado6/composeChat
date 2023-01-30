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

    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState.Loaded)
    val uiState = _uiState.asStateFlow()


}