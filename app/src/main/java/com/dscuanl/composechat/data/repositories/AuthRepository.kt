package com.dscuanl.composechat.data.repositories

import com.dscuanl.composechat.data.network.AuthService
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

object AuthRepository {

    val user = AuthService.currentUser

    suspend fun checkSession(): Boolean {
        return AuthService.checkSession()
    }

    suspend fun signInWithGoogle(credential: AuthCredential) {
        val result = AuthService.signInWithGoogle(credential).await()
        if (result.user == null) throw Exception("User not found")
        AuthService.setCurrentUser(result.user!!)
        registerUserIfDoesntExists(result.user!!)
    }

    private suspend fun registerUserIfDoesntExists(user:FirebaseUser){

    }

}