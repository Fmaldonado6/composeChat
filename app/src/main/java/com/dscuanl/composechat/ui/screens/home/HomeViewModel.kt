package com.dscuanl.composechat.ui.screens.home

import androidx.lifecycle.ViewModel
import com.dscuanl.composechat.data.repositories.AuthRepository

class HomeViewModel : ViewModel(){

    val users = AuthRepository.users

}