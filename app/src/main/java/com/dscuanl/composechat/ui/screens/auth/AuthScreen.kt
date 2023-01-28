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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dscuanl.composechat.BuildConfig
import com.dscuanl.composechat.ui.navigation.Screens
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun AuthScreen(
    navController: NavController,
    vm: AuthScreenViewModel
) {

    val launcher = rememberFirebaseAuthLauncher(
        onAuthComplete = {
            vm.signIn(it)
        }, onAuthError = {

        }
    )

    val context = LocalContext.current


    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {

            val uiState by vm.uiState.collectAsState()

            when (uiState) {
                AuthUiState.Initial -> AuthInitial {
                    val gso =
                        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(BuildConfig.GCP_KEY)
                            .requestEmail()
                            .build()
                    val googleSignInClient = GoogleSignIn.getClient(context, gso)
                    launcher.launch(googleSignInClient.signInIntent)
                }
                AuthUiState.Loading -> AuthLoading()
                AuthUiState.Error -> AuthError()
                AuthUiState.Success -> {
                    LaunchedEffect(Unit) {
                        navController.navigate(Screens.Home.route)
                    }
                    AuthLoading()
                }

            }

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

}

@Composable
fun AuthError() {

}

@Composable
fun AuthSuccess() {

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