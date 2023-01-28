package com.dscuanl.composechat.data.network

import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow


object AuthService {

    private val firebaseAuth = FirebaseAuth.getInstance()

    private val _currentUser: MutableStateFlow<FirebaseUser?> = MutableStateFlow(null)
    val currentUser = _currentUser.asStateFlow()

    suspend fun checkSession(): Boolean {
        val user = firebaseAuth.currentUser
        if (user != null) {
            _currentUser.emit(user)
            return true
        }
        return false
    }

    fun signOut(){
        firebaseAuth.signOut()
    }

    fun signInWithGoogle(googleCredential: AuthCredential): Task<AuthResult> {
        return firebaseAuth.signInWithCredential(googleCredential)
    }

    suspend fun setCurrentUser(user: FirebaseUser) {
        _currentUser.emit(user)
    }


}