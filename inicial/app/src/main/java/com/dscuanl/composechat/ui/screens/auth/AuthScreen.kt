package com.dscuanl.composechat.ui.screens.auth

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider

@Composable
fun AuthScreen(
    navController: NavController,
    vm: AuthScreenViewModel
) {



    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {



        }

    }
}

@Composable
fun AuthInitial(onSignInClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .fillMaxHeight(),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth(),
            elevation = 5.dp
        ) {

            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    "Iniciar sesion",
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center
                    )
                )
                Divider(
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 10.dp)
                )
                Button(onClick = { onSignInClicked() }) {
                    Text("Iniciar Sesión con Google")
                }
            }
        }
    }

}

@Composable
fun AuthLoading() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun AuthError() {
    Text("Error")
}


@Composable
fun rememberFirebaseAuthLauncher(
    onAuthComplete: (AuthCredential) -> Unit,
    onAuthError: (ApiException) -> Unit
): ManagedActivityResultLauncher<Intent, ActivityResult> {
    return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
            onAuthComplete(credential)
        } catch (e: ApiException) {
            onAuthError(e)
        }
    }
}



